package br.com.javafxsecurekey.model.dao;

import br.com.javafxsecurekey.model.domain.Log;
import br.com.javafxsecurekey.model.factory.ConnectionFactory;
import br.com.javafxsecurekey.model.util.Arvore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.LinkedList;

/**
 * Essa Classe faz a manipulação dos dados sobre o login de usuários com o banco de dados
 * @author Bruno Sousa da Silva
 */
public class LogDAO {

    /**
     * O método executa o INSERT no banco de dados
     */
    public void saveLogin(Log log) {

        String sql = "INSERT INTO log(idUsuario, dtLogin) VALUES (?, ?)";

        log.setDtLogin(new Timestamp(System.currentTimeMillis()));

        Connection conn = null;

        PreparedStatement pstm = null;

        try{
            //Cria conexão com o banco
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma query
            pstm = conn.prepareStatement(sql);

            //Adicionar os valores que são esperados pela Query
            pstm.setInt(1, log.getIdUsuario());
            pstm.setTimestamp(2, log.getDtLogin());

            //Executa a Query
            pstm.execute();

        }catch(Exception e) {
            e.printStackTrace();
        }finally{

            //Fecha as conexões que foram abertas com o banco de dados
            try{
                if(pstm!=null){
                    pstm.close();
                }

                if(conn!=null){
                    conn.close();
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * O método executa o INSERT no banco de dados
     */
    public void saveLogout(Log log) {

        String sql = "UPDATE log SET dtLogout = ? WHERE idLog = ?;";

        log.setDtLogout(new Timestamp(System.currentTimeMillis()));

        Connection conn = null;

        PreparedStatement pstm = null;

        try{
            //Cria conexão com o banco
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma query
            pstm = conn.prepareStatement(sql);

            //Adicionar os valores que são esperados pela Query
            pstm.setTimestamp(1, log.getDtLogout());
            pstm.setInt(2, log.getIdLog());

            //Executa a Query
            pstm.execute();

        }catch(Exception e) {
            e.printStackTrace();
        }finally{

            //Fecha as conexões que foram abertas com o banco de dados
            try{
                if(pstm!=null){
                    pstm.close();
                }

                if(conn!=null){
                    conn.close();
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    //O método executa o READ no banco de dados
    public LinkedList<Log> getLogs() {
        String sql = "SELECT L.*, U.username, U.role FROM log L JOIN usuario U ON(L.idUsuario = U.idUsuario)"; //Verificar se vai dar certo

        //Lista que armazenará os dados de logs
        LinkedList<Log> listaLogs = new LinkedList<>();

        Connection conn = null;

        PreparedStatement pstm = null;

        // Classe que vai recuperar os dados do banco.  *** SELECT ***
        ResultSet rset = null;

        try{
            //Cria a conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma query
            pstm = conn.prepareStatement(sql);

            rset = pstm.executeQuery();

            //Enquanto houver um próximo dado para ser armazenado pelo ResultSet, os comandos serão executados
            while(rset.next()){
                Log log = new Log();

                //Recupera o idLogs da log
                log.setIdLog(rset.getInt("idLog"));

                //Recupera o idUser da log
                log.setIdUsuario(rset.getInt("idUsuario"));

                //Recupera o username da consulta
                log.setUsername(rset.getString("username"));

                // Recupera a role do usuário da consulta
                log.setRole(rset.getString("role"));

                //Recupera a data do login
                log.setDtLogin(rset.getTimestamp("dtLogin"));

                //Recupera a data do logout
                log.setDtLogout(rset.getTimestamp("dtLogout"));

                //Adiciona a log com todos os dados registrados à lista de chaves
                listaLogs.add(log);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{

            try{
                //Fecha as conexões que foram abertas com o banco de dados
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

        return listaLogs;
    }


    //O método executa o READ no banco de dados  e armazena os dados em árvore
    public Arvore<Log> getLogsEmArvore() {
        String sql = "SELECT L.*, U.username, U.role FROM log L JOIN usuario U ON(L.idUsuario = U.idUsuario)"; //Verificar se vai dar certo

        //Lista que armazenará os dados de logs
        Arvore<Log> arvoreLog = new Arvore<>();

        Connection conn = null;

        PreparedStatement pstm = null;

        // Classe que vai recuperar os dados do banco.  *** SELECT ***
        ResultSet rset = null;

        try{
            //Cria a conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma query
            pstm = conn.prepareStatement(sql);

            rset = pstm.executeQuery();

            //Enquanto houver um próximo dado para ser armazenado pelo ResultSet, os comandos serão executados
            while(rset.next()){
                Log log = new Log();

                //Recupera o idLogs da log
                log.setIdLog(rset.getInt("idLog"));

                //Recupera o idUser da log
                log.setIdUsuario(rset.getInt("idUsuario"));

                //Recupera o username da consulta
                log.setUsername(rset.getString("username"));

                // Recupera a role do usuário da consulta
                log.setRole(rset.getString("role"));

                //Recupera a data do login
                log.setDtLogin(rset.getTimestamp("dtLogin"));

                //Recupera a data do logout
                log.setDtLogout(rset.getTimestamp("dtLogout"));

                //Adiciona a log com todos os dados registrados à lista de chaves
                arvoreLog.adicionar(log);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{

            try{
                //Fecha as conexões que foram abertas com o banco de dados
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

        return arvoreLog;
    }

    /**
     * O método executa o comando UPDATE no banco de dados
     */
    public void update(Log log){

        String sql = "UPDATE log SET idUsuario = ?"+
                "WHERE idLog = ?";

        Connection conn = null;

        PreparedStatement pstm = null;

        try{
            //Cria a conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma query
            pstm = conn.prepareStatement(sql);

            //Adicina os valores para atualizar
            pstm.setInt(1, log.getIdUsuario());

            //Qual o ID do registro que deseja atualizar?
            pstm.setInt(2, log.getIdLog());

            //Executa a Query
            pstm.execute();

        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            try{

                //Fechar as conexões que foram abertas
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
    }

    /**
     *  O método executa o comando DELETE no banco de dados
     */
    public void deleteByID(int idLog){

        String sql = "DELETE FROM log WHERE idLog = ?";

        Connection conn = null;

        PreparedStatement pstm = null;

        try{
            //Cria a conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma query
            pstm = conn.prepareStatement(sql);

            //Passando o id do registro que será excluído
            pstm.setInt(1, idLog);

            //Executa a Query
            pstm.execute();

        }catch(Exception e){
            e.printStackTrace();
        }finally{

            try{
                //Fechar as conexões que foram abertas
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
    }

    /**
     * O método realiza um READ na view ultimo_logado e pega o último usuário que realizou o login
     * @return
     */
    public Log getUltimoLogado() {
        String sql = "SELECT idLog, username, role FROM ultimo_logado;";

        //Lista que armazenará os dados de log
        Log log = new Log();

        Connection conn = null;

        PreparedStatement pstm = null;

        // Classe que vai recuperar os dados do banco.  *** SELECT ***
        ResultSet rset = null;

        try{
            //Cria a conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma query
            pstm = conn.prepareStatement(sql);

            rset = pstm.executeQuery();

            //Enquanto houver um próximo dado para ser armazenado pelo ResultSet, os comandos serão executados
            while(rset.next()){

                //Recupera o idLogs do último usuário logado
                log.setIdLog(rset.getInt("idLog"));

                //Recupera o username do último usuário logado
                log.setUsername(rset.getString("username"));

                log.setRole(rset.getString("role"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{

            try{
                //Fecha as conexões que foram abertas com o banco de dados
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

        return log;
    }

}
