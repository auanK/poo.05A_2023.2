import java.util.*;

class MsgException extends RuntimeException {
    public MsgException(String message) {
        super(message);
    }
}

interface Autenticavel {
    public void logar(String senha);
    public void deslogar();
    
    public String getNome();
    
    public void setSenha(String senha);
    public String getSenha();
    
    public void setLogado(boolean logado);
    public String getLogado();
}

abstract class Funcionario {
    protected String nome;
    protected int bonus;
    protected int diarias;
    protected int maxDiarias;

    public Funcionario(String nome) {
        this.nome = nome;
        this.bonus = 0;
        this.diarias = 0;
        this.maxDiarias = 0;
    }

    public String getNome() {
        return this.nome;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    //se atingiu o máximo, lance uma MsgException
    //se não atingiu o máximo, adicione mais uma diária
    public void addDiaria() {
        if ( this.diarias >= this.maxDiarias ) {
            throw new MsgException("fail: limite de diarias atingido");
        }
        
        this.diarias ++;
    }

    //retorna bonus + diarias * 100
    public int getSalario() {
        return this.bonus + this.diarias * 100;
    }
}

class FuncionarioAutenticavel extends Funcionario implements Autenticavel {
    private String senha;
    private boolean logado;

    public FuncionarioAutenticavel(String nome) {
        super(nome);
        this.senha = "indefinida";
        this.logado = false;
    }
    
    public void logar( String senha ) {
        if(this.senha.equals(senha)){
            logado = true;
        }else{
            logado = false;
            throw new MsgException("fail: senha invalida");
        }
    }
    
    public void deslogar() {
        logado = false;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public String getSenha() {
        return senha;
    }
    public void setLogado(boolean logado) {
        this.logado = logado;
    }
    public String getLogado() {
        return this.logado ? "online" : "offline";
    }
}

class Professor extends FuncionarioAutenticavel {
    protected String classe;

    //inicializa classe e muda maxDiarias para 2
    public Professor(String nome, String classe) {
        super( nome );
        this.classe = classe;
        this.maxDiarias = 2;
    }

    public String getClasse() {
        return this.classe;
    }
    
    public int getValorClasse() {
        switch (this.getClasse()) {
            case "A":
                return 3000;
            case "B":
                return 5000;
            case "C":
                return 7000;
            case "D":
                return 9000;
            case "E":
                return 11000;
            default:
                return 0;
        }
    }

    //lógica do salário do professor
    //usa o super.getSalario() para pegar bonus e diarias
    @Override
    public int getSalario() {
        return super.getSalario() + getValorClasse();
    }

    @Override
    public String toString() {
        return "prof:" + this.getNome() + ":" + this.getClasse() + ":" + this.getSalario();
    }
}

class STA extends FuncionarioAutenticavel {
    protected int nivel;

    //inicializa nivel e muda maxDiarias para 1
    public STA(String nome, int nivel) {
        super( nome );
        this.nivel = nivel;
        this.maxDiarias = 1;
    }
    
    public int getNivel() {
        return this.nivel;
    }

    //lógica do salário do sta
    //usa o super.getSalario() para pegar bonus e diarias
    @Override
    public int getSalario() {
        return super.getSalario() + 3000 + 300 * this.getNivel();
    }

    @Override
    public String toString() {
        return "sta:" + this.getNome() + ":" + this.getNivel() + ":" + this.getSalario();
    }
}

class Terceirizado extends Funcionario {
    protected int horas;
    protected boolean isSalubre;

    public Terceirizado(String nome, int horas, String isSalubre) {
        super( nome );
        this.horas = horas;
        this.isSalubre = isSalubre.equals("sim") ? true : false;
    }

    public int getHoras() {
        return this.horas;
    }

    public String getIsSalubre() {
        return this.isSalubre ? "sim" : "nao";
    }

    //lance uma MsgException com um texto diferente
    @Override
    public void addDiaria() {
        throw new MsgException("fail: terc nao pode receber diaria");
    }

    //lógica do salário do terceirizado
    //usa o super.getSalario() para pegar bonus e diarias
    @Override
    public int getSalario() {
        return super.getSalario() + 4 * this.getHoras() + (this.isSalubre ? 500 : 0);
    }

    @Override
    public String toString() {
        return "ter:" + this.getNome() + ":" + this.getHoras() + ":" + this.getIsSalubre() + ":" + this.getSalario();
    }
}

class Aluno implements Autenticavel {
    private String nome;
    private String curso;
    private int bolsa;

    private String senha;
    private boolean logado;

    public Aluno(String nome, String curso, int bolsa) {
        this.nome = nome;
        this.curso = curso;
        this.bolsa = bolsa;
        this.senha = "indefinida";
        this.logado = false;
    }
    
    public String getNome() {
        return nome;
    }
    public String getCurso() {
        return curso;
    }
    public int getBolsa() {
        return bolsa;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    public String getSenha() {
        return senha;
    }
    public void setLogado(boolean logado) {
        this.logado = logado;
    }
    public String getLogado() {
        return this.logado ? "online" : "offline";
    }

    public void logar( String senha ) {
        if(this.senha.equals(senha)){
            logado = true;
        }else{
            logado = false;
            throw new MsgException("fail: senha incorreta");
        }
    }
    public void deslogar() {
        this.logado = false;
    }

    @Override
    public String toString() {
        return "alu:" + this.getNome() + ":" + this.getCurso() + ":" + this.getBolsa();
    }
}

class UFC {
    private Map<String, Funcionario> funcionarios;
    private Map<String, Aluno> alunos;

    public UFC() {
        this.funcionarios = new TreeMap<String, Funcionario>();
        this.alunos = new TreeMap<String, Aluno>();
    }

    @Override
    public String toString() {
        String s = "";
        for ( Funcionario f : this.funcionarios.values() ) {
            s += f + "\n";
        }
        for ( Aluno a : this.alunos.values() ) {
            s += a + "\n";
        }
        return s;
    }

    public Funcionario getFuncionario(String nome) {
        return this.funcionarios.get( nome );
    }

    public void addFuncionario(Funcionario funcionario) {
        this.funcionarios.put( funcionario.getNome(), funcionario );
    }
    
    public void rmFuncionario(String nome) {
        this.funcionarios.remove( nome );
    }

    //reparte o bonus para todos os funcionarios
    public void setBonus(int bonus) {
        if ( this.funcionarios.size() == 0 ) {
            throw new MsgException("fail: sem funcionarios");
        }

        int eachBonus = bonus / this.funcionarios.size();
        for ( Funcionario f : this.funcionarios.values() ) {
            f.setBonus( eachBonus );
        }
    }

    public Aluno getAluno(String nome) {
        return this.alunos.get(nome);
    }

    public void addAluno(Aluno aluno) {
        this.alunos.put(aluno.getNome(), aluno);
    }
    
    public void rmAluno(String nome) {
        this.alunos.remove(nome);
    }
}


class Sistema {
    private UFC ufc;
    private Map<String, Autenticavel> usuarios;

    public Sistema() {
        this.ufc = new UFC();
        this.usuarios = new TreeMap<String, Autenticavel>();
    }

    public UFC getUFC() {
        return ufc;
    }

    public Autenticavel getUsuario(String nome) {
        return this.usuarios.get(nome);
    }

    public void addUsuario(String nome, String senha) {
        if(ufc.getFuncionario(nome) != null)
        {
            if(ufc.getFuncionario(nome) instanceof Terceirizado){
                throw new MsgException("fail: terc nao pode ser cadastrado no sistema");
            }
            ((Autenticavel)ufc.getFuncionario(nome)).setSenha(senha);
            this.usuarios.put(nome, (Autenticavel) ufc.getFuncionario(nome));
        }
        else if(ufc.getAluno(nome) != null)
        {
            ((Autenticavel)ufc.getAluno(nome)).setSenha(senha);
            this.usuarios.put(nome, ufc.getAluno(nome));
        }
        else
        {
            throw new MsgException("fail: " + nome + " nao encontrado");
        }
    }

    public void rmUsuario(String nome) {
        if(getUsuario(nome) != null)
        {
            this.usuarios.remove(nome);
        }
        else
        {
            throw new MsgException("fail: usuario " + nome + " nao encontrado");
        }
    }
    
    public void logar(String nome, String senha) {
        if(getUsuario(nome) != null){
            getUsuario(nome).logar(senha);
        }
        else
        {
            throw new MsgException("fail: usuario " + nome + " nao encontrado");
        }
    }
    
    public void deslogar(String nome) {
        if(getUsuario(nome) != null){
            this.usuarios.get(nome).deslogar();
        }
        else
        {
            throw new MsgException("fail: usuario " + nome + " nao encontrado");
        }
    }
    
    public void deslogarTodos() {
        for(String key : usuarios.keySet()){
            deslogar(key);
        }
    }

    public String showUser( String nome ) {
        if(getUsuario(nome) != null)
        {
            return "" + getUsuario(nome) + ":" + getUsuario(nome).getSenha() + ":" + getUsuario(nome).getLogado();
        }
        else
        {
            throw new MsgException("fail: usuario " + nome + " nao encontrado");
        }
    }

    @Override
    public String toString() {
        String s = "";
        for ( Autenticavel u : this.usuarios.values() ) {
            s += showUser(u.getNome()) + "\n";
        }
        return s;
    }
}

public class Solver {
    public static void main(String[] arg) {
        Sistema sis = new Sistema();

        while (true) {
            String line = input();
            println("$" + line);
            String[] args = line.split(" ");

            try {
                if      (args[0].equals("end"))           { break; }
                else if (args[0].equals("addProf"))       { sis.getUFC().addFuncionario(new Professor(args[1], args[2])); }
                else if (args[0].equals("addSta"))        { sis.getUFC().addFuncionario(new STA(args[1], (int) number(args[2]))); }
                else if (args[0].equals("addTer"))        { sis.getUFC().addFuncionario(new Terceirizado(args[1], (int) number(args[2]), args[3])); }
                else if (args[0].equals("rm"))            { sis.getUFC().rmFuncionario(args[1]); }
                else if (args[0].equals("rmFunc"))        { sis.getUFC().rmFuncionario(args[1]); }
                else if (args[0].equals("showAll"))       { print(sis.getUFC()); }
                else if (args[0].equals("show"))          { println(sis.getUFC().getFuncionario(args[1])); }
                else if (args[0].equals("showFunc"))      { println(sis.getUFC().getFuncionario(args[1])); }
                else if (args[0].equals("addDiaria"))     { sis.getUFC().getFuncionario(args[1]).addDiaria(); }
                else if (args[0].equals("setBonus"))      { sis.getUFC().setBonus((int) number(args[1])); }
                else if (args[0].equals("addAlu"))        { sis.getUFC().addAluno( new Aluno( args[1], args[2], (int) number(args[3]) ) ); }
                else if (args[0].equals("rmAlu"))         { sis.getUFC().rmAluno(args[1]); }
                else if (args[0].equals("showAlu"))       { println(sis.getUFC().getAluno(args[1])); }
                else if (args[0].equals("addUser"))       { sis.addUsuario( args[1], args[2] ); }
                else if (args[0].equals("rmUser"))        { sis.rmUsuario(args[1]); }
                else if (args[0].equals("showUser"))      { println( sis.showUser(args[1]) ); }
                else if (args[0].equals("showAllUsers"))  { print(sis); }
                else if (args[0].equals("logar"))         { sis.logar( args[1], args[2] ); }
                else if (args[0].equals("deslogar"))      { sis.deslogar(args[1]); }
                else if (args[0].equals("deslogarTodos")) { sis.deslogarTodos(); }
                else                                      { println("fail: comando invalido"); }
            } catch (MsgException me) {
                println(me.getMessage());
                // e.printStackTrace(System.out);
            }
        }
    }

    private static Scanner scanner = new Scanner(System.in);
    private static String  input()                { return scanner.nextLine();        }
    private static double  number(String value)   { return Double.parseDouble(value); }
    public  static void    println(Object value)  { System.out.println(value);        }
    public  static void    print(Object value)    { System.out.print(value);          }
}