/*
 * The MIT License
 *
 * Copyright 2024 Bruno Sousa da Silva.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.com.javafxsecurekey.model.dao;

import br.com.javafxsecurekey.model.factory.ConnectionFactory;
import br.com.javafxsecurekey.model.domain.Chave;
import br.com.javafxsecurekey.model.domain.Historico;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

/**
 * Essa Classe faz a manipulação dos dados sobre chaves com o banco de dados
 * @author Bruno Sousa da Silva
 */
public class ChaveDAO {

    public static boolean result;

    public static boolean getResult()
    {
        return result;
    }

    public static void setDefaultResult()
    {
        result = false;
    }

    /**
     * O método executa o INSERT no banco de dados
     */
    public static void save(Chave chave) {

        String sql = "INSERT INTO chaves(numeroChave, sala, observacoes, quantChave, status, bloco_predio, possuiReserva) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;

        PreparedStatement pstm = null;


        try{
            //Cria conexão com o banco
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma query
            pstm = conn.prepareStatement(sql);

            //Adicionar os valores que são esperados pela Query
            pstm.setInt(1, chave.getNumeroChave());
            pstm.setString(2, chave.getSala());
            pstm.setString(3, chave.getObservacoes());
            pstm.setInt(4, chave.getQuantChave());
            pstm.setString(5, chave.getStatus());
            pstm.setString(6, chave.getBloco());
            pstm.setString(7, chave.getPossuiReserva());

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
    public static LinkedList<Chave> getChave() {

        String sql = "SELECT * FROM chaves";

        LinkedList<Chave> listaChave = new LinkedList<>();

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
                Chave chave = new Chave();

                //Recupera o id da chave
                chave.setIdChave(rset.getInt("idChave"));

                //Recupera o número da chave
                chave.setNumeroChave(rset.getInt("numeroChave"));

                //Recupera a sala da chave
                chave.setSala(rset.getString("sala"));

                //Recupera o Bloco/Predio que pertence a chave
                chave.setBloco(rset.getString("bloco_predio"));

                //Recupera as observações da chave
                chave.setObservacoes(rset.getString("observacoes"));

                //Recupera a quantidade da respectiva chave
                chave.setQuantChave(rset.getInt("quantChave"));

                //Recupera o status da chave
                chave.setStatus(rset.getString("status"));

                //Recupera o tipo da chave
                chave.setPossuiReserva(rset.getString("possuiReserva"));

                //Adiciona a chave com todos os dados registrados à lista de chaves
                listaChave.add(chave);
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

        return listaChave;
    }

    /**
     * O método executa o comando UPDATE no banco de dados
     */
    public void update(Chave chave){

        String sql = "UPDATE chaves SET numeroChave = ?, sala = ?, observacoes = ?, quantChave = ?, status = ?, bloco-predio = ?, possuiReserva = ? WHERE idChave = ?";

        Connection conn = null;

        PreparedStatement pstm = null;

        try{
            //Cria a conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma query
            pstm = conn.prepareStatement(sql);

            //Adicina os valores para atualizar
            pstm.setInt(1, chave.getNumeroChave());
            pstm.setString(2,chave.getSala());
            pstm.setString(3, chave.getObservacoes());
            pstm.setInt(4, chave.getQuantChave());
            pstm.setString(5, chave.getStatus());
            pstm.setString(6, chave.getBloco());
            pstm.setString(7, chave.getPossuiReserva());

            //Qual o ID do registro que deseja atualizar?
            pstm.setInt(8, chave.getIdChave());

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
    public void deleteByID(int idChave){

        String sql = "DELETE FROM chaves WHERE idChave = ?";

        Connection conn = null;

        PreparedStatement pstm = null;

        try{
            //Cria a conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma query
            pstm = conn.prepareStatement(sql);

            //Passando o id do registro que será excluído
            pstm.setInt(1, idChave);

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
     *
     * @param idChave
     */
    public void updateStatusChave(int idChave){

        String sql = "UPDATE chaves SET status = 'INDISPONÍVEL' WHERE idChave = ? AND quantChave = 0;";

        Connection conn = null;

        PreparedStatement pstm = null;

        try{
            //Cria a conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma query
            pstm = conn.prepareStatement(sql);

            //
            pstm.setInt(1, idChave);

            //
            pstm.execute();

        }catch(Exception e){
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
     * Método para emprestar a chave
     *
     * verificar a classe de parâmetro para salvar o emprestimo no histórico
     */
    public static void emprestarChave(Historico historico){

        VerifyDAO verifyDAO = new VerifyDAO();

        result = false;

        //
        if(verifyDAO.verifyStatusChave(historico.getIdChave())){

            String sql = "UPDATE chaves SET quantChave = quantChave - 1 WHERE idChave = ?";

            Connection conn = null;

            PreparedStatement pstm = null;

            try{
                //Cria a conexão com o banco de dados
                conn = ConnectionFactory.createConnectionToMySQL();

                //Criamos uma PreparedStatement para executar uma query
                pstm = conn.prepareStatement(sql);

                //
                pstm.setInt(1, historico.getIdChave());

                //Executa a Query
                int rowsUpdated = pstm.executeUpdate();

                if(rowsUpdated > 0)
                {
                    //Após executar a query que realiza o empréstimo da chave, salvamos a transação no histórico
                    HistoricoDAO.save(historico);

                    if(HistoricoDAO.getResult())
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "A chave foi emprestáda com sucesso!");
                        alert.showAndWait();
                    }
                    else
                    {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Não foi possível salvar o histórico do empréstimo!\nOcorreu algum erro!");
                        alert.showAndWait();
                    }
                    HistoricoDAO.setDefaultResult();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Não foi possível emprestár a chave!\nOcorreu algum erro!");
                    alert.showAndWait();
                }

            }catch(Exception e){
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
    }

    /**
     * Verificar usabilidade desse método de devolução e do método de updateDataFechamento do HistoricoDAO
     * @param historico
     */
    public void devolverChave(Historico historico){

        String sqlUpdate = "UPDATE chaves SET quantChave = quantChave + 1, status = 'DISPONÍVEL' WHERE idChave = ?";

        Connection connUpdate = null;

        PreparedStatement pstmUpdate = null;

        try{
            //
            connUpdate = ConnectionFactory.createConnectionToMySQL();

            //
            pstmUpdate = connUpdate.prepareStatement(sqlUpdate);

            //
            pstmUpdate.setInt(1, historico.getIdChave());

            //
            int rowsUpdated = pstmUpdate.executeUpdate();

            // Se alguma linha for atualizada no banco de dados com a execução da consulta,
            // então salvamos no histórico a data de devolução
            if(rowsUpdated > 0){
                //
                HistoricoDAO historicoDAO = new HistoricoDAO();

                historicoDAO.updateDataFechamento(historico);
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING, "Ocorreu algum erro e a chave não foi devolvida!");
                alert.showAndWait();
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                //
                if(pstmUpdate!=null){
                    pstmUpdate.close();
                }

                if(connUpdate!=null){
                    connUpdate.close();
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
