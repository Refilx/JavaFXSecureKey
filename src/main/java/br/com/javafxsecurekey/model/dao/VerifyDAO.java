package br.com.javafxsecurekey.model.dao;

import br.com.javafxsecurekey.model.domain.Chave;
import br.com.javafxsecurekey.model.domain.Log;
import br.com.javafxsecurekey.model.domain.Usuario;
import br.com.javafxsecurekey.model.factory.ConnectionFactory;
import org.jasypt.util.password.BasicPasswordEncryptor;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Essa Classe faz a verificações de dados de acordo com o que está armazenado no banco de dados
 * @author Bruno Sousa da Silva
 */
public class VerifyDAO {

    /**
     *  O método verifica se existe um username no banco de dados identico ao informado no parâmetro
     * @param username
     * @return
     */
    public boolean verifyUsername(String username) {

        String sql = "SELECT username FROM usuario";

        //
        boolean resultadoVerify = true;

        Connection conn = null;

        //
        PreparedStatement pstm = null;

        //
        ResultSet rset = null;

        try{
            //
            conn = ConnectionFactory.createConnectionToMySQL();

            //
            pstm = conn.prepareStatement(sql);

            //
            rset = pstm.executeQuery();

            //
            while (rset.next()){
                //
                String userDoBanco = rset.getString("username");

                //
                if(username.equalsIgnoreCase(userDoBanco)){
                    resultadoVerify = false;
                    break;
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                //
                if(rset!=null){
                    rset.close();
                }

                if(pstm!=null){
                    pstm.close();
                }

                if(conn!=null){
                    conn.close();
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return resultadoVerify;
    }

    /**
     * O método abaixo verifica se a role do ultimo usuário que realizou o login é um Administrador
     * @return
     */
    public boolean verifySuperUser() {

        String sql = "SELECT * FROM ultimo_logado";

        boolean resultadoVerify = false;

        Connection conn = null;

        PreparedStatement pstm = null;

        ResultSet rset = null;

        try{
            //
            conn = ConnectionFactory.createConnectionToMySQL();

            //
            pstm = conn.prepareStatement(sql);

            //
            rset = pstm.executeQuery();

            //
            rset.next();

            //
            String role = rset.getString("role");

            if(role.equalsIgnoreCase("Administrador")){
                resultadoVerify = true;
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try{
                //
                if(rset!=null){
                    rset.close();
                }

                if(pstm!=null){
                    pstm.close();
                }

                if(conn!=null){
                    conn.close();
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return resultadoVerify;
    }

    /**
     *  O método verifica se existe um CPF no banco de dados identico ao informado no parâmetro
     *  Para não permitir o cadastro de pessoas com o CPF igual, por isso retorna false (caso encontre)
     * @param cpf
     * @return
     */
    public boolean verifyCPF(String cpf) {

        String sql = "SELECT CPF FROM pessoa";

        //
        boolean resultadoVerify = true;

        Connection conn = null;

        //
        PreparedStatement pstm = null;

        //
        ResultSet rset = null;

        try{
            //
            conn = ConnectionFactory.createConnectionToMySQL();

            //
            pstm = conn.prepareStatement(sql);

            //
            rset = pstm.executeQuery();

            //
            while (rset.next()){
                //
                String cpfDoBanco = rset.getString("CPF");

                //
                if(cpf.equalsIgnoreCase(cpfDoBanco)){
                    resultadoVerify = false;
                    break;
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                //
                if(rset!=null){
                    rset.close();
                }

                if(pstm!=null){
                    pstm.close();
                }

                if(conn!=null){
                    conn.close();
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return resultadoVerify;

    }

    /**
     * O método abaixo valida a senha que será passada pelo usuário ao tentar fazer login
     * @param user
     * @param pass
     * @return
     */
    public boolean verifyPass(String user, String pass){

        String sql = "SELECT idUsuario, password FROM usuario WHERE username = ?";

        boolean resultadoValidacao = false;

        Connection conn = null;

        PreparedStatement pstm = null;

        // Classe que vai recuperar os dados do banco.  *** SELECT ***
        ResultSet rset = null;

        try{
            //Cria conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma query
            pstm = conn.prepareStatement(sql);

            //Atribui a query o username que será buscado no banco
            pstm.setString(1, user);

            rset = pstm.executeQuery();

            if(rset.next()){

                //Criamos um usuário
                Usuario usuario = new Usuario();

                //Inicializamos o encriptador
                BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();

                //Neste ponto pegamos a senha do respectivo usuário cadatrado no banco de dados
                usuario.setPassword(rset.getString("password"));

                /**
                 Verificamos se a senha digitada pelo usuário na tela de login (que será passada como parâmetro do método)
                 utilizando a classe de criptografia de dados para comparar com  senha criptografada do usuário guardada no banco de dados

                 se a senha estiver correta, validamos o login
                 e armazenamos o usuário que fez login pegando o id do respectivo usuário
                 */
                if(passwordEncryptor.checkPassword(pass, usuario.getPassword())){
                    //Armazenamos o id do respectivo usuário
                    usuario.setIdUsuario(rset.getInt("idUsuario"));

                    //Criamos neste ponto as classes que irão armazenar os dados do usuário que está acessando
                    LogDAO logDAO = new LogDAO();
                    Log log = new Log();

                    //Armazenamos o idUser e a data do log no objeto log e salvamos no banco de dados
                    log.setIdUsuario(usuario.getIdUsuario());

                    logDAO.saveLogin(log);

                    resultadoValidacao = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Senha incorreta! Tente novamente.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "O nome de usuário digitado está incorreto ou\nNão existe usuário com esse username cadastrado!");
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally {

            try{
                //
                if(rset!=null){
                    rset.close();
                }

                if(pstm!=null){
                    pstm.close();
                }

                if(conn!=null){
                    conn.close();
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return resultadoValidacao;
    }

    /**
     * Verifica se o email passado por parâmetro existe no banco de dados
     * @param email
     * @return true (se existir no banco) e false (se NÃO existir no banco)
     */
    public static boolean verifyEmail(String email) {
        String sql = "SELECT email FROM pessoa WHERE email = ?";

        boolean resultVerify = false;

        Connection conn =  null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = ConnectionFactory.createConnectionToMySQL();

            pstm = conn.prepareStatement(sql);

            pstm.setString(1, email);

            rset = pstm.executeQuery();

            if(rset.next()) {
                resultVerify = true;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(conn != null)
                    conn.close();

                if(pstm != null)
                    pstm.close();

                if(rset != null)
                    rset.close();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return resultVerify;
    }

    /**
     * Verifica o status da chave para saber se ela está disponível
     */
    public static boolean verifyStatusChave(int idChave){

        String sql = "SELECT status, quantChave, possuiReserva FROM chaves WHERE idChave = ?";

        boolean resultadoVerify = false;

        Connection conn = null;

        PreparedStatement pstm = null;

        ResultSet rset = null;

        try{
            //Cria a conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma query
            pstm = conn.prepareStatement(sql);

            //
            pstm.setInt(1, idChave);

            //
            rset = pstm.executeQuery();

            //
            if(rset.next()) {
                //
                Chave chave = new Chave();

                //Variável que armazenará o valor do status da chave armazenada no banco de dados
                chave.setStatus(rset.getString("status"));

                //Variável que armazenará a quantidade de chaves que estarão na portaria no momento em que for realizado o emprestimo
                chave.setQuantChave(rset.getInt("quantChave"));

                //Variável que armazenará o possuiReserva da chave (chave principal ou chave reserva) armazenada no banco de dados
                chave.setPossuiReserva(rset.getString("possuiReserva"));

                //Se o status da chave for disponível, a quantidade for maior ou igual que 1 (um), o resultado recebe um valor true
                if (
                        (chave.getQuantChave() > 1 &&
                                chave.getStatus().equalsIgnoreCase("DISPONÍVEL") &&
                                chave.getPossuiReserva().equalsIgnoreCase("Sim"))
                                ||
                                (chave.getQuantChave() == 1 &&
                                chave.getStatus().equalsIgnoreCase("DISPONÍVEL") &&
                                chave.getPossuiReserva().equalsIgnoreCase("Não"))


                ) {
                    resultadoVerify = true;
                }
                //Tratamento de chaves com a quantidade igual a 0 (zero)
                else if (chave.getQuantChave() == 0) {
                    //
                    ChaveDAO chaveDAO = new ChaveDAO();

                    //
                    chaveDAO.updateStatusChave(idChave);

                    //Mensagem de erro ao tentar emprestar a chave, estando ela indisponível
                    JOptionPane.showMessageDialog(null, "Você não pode emprestar esta chave, pois ela está INDISPONÍVEL!",
                            "ERRO AO EMPRESTAR CHAVE!", JOptionPane.ERROR_MESSAGE);
                }
                //Tratamento de emprestimo de chave reserva
                else if (chave.getQuantChave() == 1 && chave.getPossuiReserva().equalsIgnoreCase("Sim")) {

                    //Mensagem de confirmação de emprestimo de chave reserva
                    int opcao = JOptionPane.showOptionDialog(null, "Você está emprestando uma Chave RESERVA! \n Tem certeza que deseja continuar?",
                            "Chave Reserva Identificada", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, null);

                    if (opcao == 0) {
                        resultadoVerify = true;
                    } else {
                        JOptionPane.showMessageDialog(null, "A Chave NÃO foi emprestada!",
                                "Cancelado!", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{

                //Fechar as conexões que foram abertas
                if(rset!=null){
                    rset.close();
                }

                if(pstm!=null){
                    pstm.close();
                }

                if(conn!=null) {
                    conn.close();
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return resultadoVerify;
    }

    /**
     * Verifica se a mesma pessoa está tentando pegar uma chave igual
     */
    public boolean verifyAlreadyBorrowed(int idChave, int idPessoa){

        String sql = "CALL verificaPessoaJaPegou( ?, ?);";

        boolean resultadoVerify = false;

        Connection conn = null;

        PreparedStatement pstm = null;

        ResultSet rset = null;

        try{
            //Cria a conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma query
            pstm = conn.prepareStatement(sql);

            //
            pstm.setInt(1, idChave);
            pstm.setInt(2, idPessoa);

            //
            rset = pstm.executeQuery();

            //
            rset.next();

            boolean result = rset.getBoolean("result");

            // Se a pessoa não tiver pego a chave emprestada, será possibilitado o emprestimo
            if (result) {
                resultadoVerify = true;
            }
            // Se a pessoa já tiver pego a chave emprestada, então não poderá pegar a mesma chave emprestada novamente
            // enquanto não devolver a que foi pega
            else {
                //Mensagem de erro ao tentar emprestar a chave, estando ela indisponível
                JOptionPane.showMessageDialog(null, "Essa pessou já está com essa chave!",
                        "ERRO AO EMPRESTAR CHAVE!", JOptionPane.ERROR_MESSAGE);
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{

                //Fechar as conexões que foram abertas
                if(rset!=null){
                    rset.close();
                }

                if(pstm!=null){
                    pstm.close();
                }

                if(conn!=null) {
                    conn.close();
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return resultadoVerify;
    }

}
