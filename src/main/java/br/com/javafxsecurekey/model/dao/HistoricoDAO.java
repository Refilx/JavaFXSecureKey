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
import br.com.javafxsecurekey.model.domain.Historico;
import br.com.javafxsecurekey.model.util.Arvore;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Essa Classe faz a manipulação dos dados sobre o histórico de emprestimo de chaves com o banco de dados
 * @author Bruno Sousa da Silva
 */
public class HistoricoDAO {

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
     *
     * @param historico
     */
    public static void save(Historico historico){

        String sql = "INSERT INTO historico(idChave, idPessoa, observacoes, status, dataAbertura) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;

        PreparedStatement pstm = null;

        result = false;

        try{
            //
            conn = ConnectionFactory.createConnectionToMySQL();

            //
            pstm = conn.prepareStatement(sql);

            //
            pstm.setInt(1, historico.getIdChave());
            pstm.setInt(2, historico.getIdPessoa());
            pstm.setString(3, historico.getObservacoes());
            pstm.setString(4, historico.getStatus());
            pstm.setTimestamp(5, historico.getDataAbertura());

            //
            pstm.execute();

            result = true;
        }catch(Exception e){
            e.printStackTrace();
        }finally{

            try{
                //
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
     * @param historico
     */
    public void update(Historico historico){

        String sql = "UPDATE historico SET idChave = ?, observacoes = ?, status = ?"+
                "WHERE idHistorico = ?";

        Connection conn = null;

        PreparedStatement pstm = null;

        try{
            //
            conn = ConnectionFactory.createConnectionToMySQL();

            //
            pstm = conn.prepareStatement(sql);

            //
            pstm.setInt(1, historico.getIdChave());
            pstm.setString(2, historico.getObservacoes());
            pstm.setString(3, historico.getStatus());

            //
            pstm.setInt(4, historico.getIdHistorico());

            //
            pstm.execute();

        }catch(Exception e){
            e.printStackTrace();
        }finally{

            try{
                //
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
     * @param historico
     */
    public void updateDataFechamento(Historico historico){

        String sql = "UPDATE historico SET dataFechamento = ?, status = 'ENCERRADO' WHERE idHistorico = ?";

        historico.setDataFechamento(new Timestamp(System.currentTimeMillis()));

        Connection conn = null;

        PreparedStatement pstm = null;

        try{
            //
            conn = ConnectionFactory.createConnectionToMySQL();

            //
            pstm = conn.prepareStatement(sql);

            //
            pstm.setTimestamp(1, historico.getDataFechamento());

            //
            pstm.setInt(2, historico.getIdHistorico());

            //
            pstm.execute();

        }catch(Exception e){
            e.printStackTrace();
        }finally{

            try{
                //
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
     * @return
     */
    public List<Historico> getHistorico(){

        String sql = "SELECT * FROM consulta_historico";

        List<Historico> listaHistorico = new ArrayList<Historico>();

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
            while(rset.next()){

                //
                Historico historico = new Historico();

                //
                historico.setIdHistorico(rset.getInt("idHistorico"));

                //
                historico.setIdChave(rset.getInt("idChave"));

                //
                historico.setNumeroChave(rset.getInt("numerochave"));

                //
                historico.setNome(rset.getString("nome"));

                //
                historico.setCargo(rset.getString("cargo"));

                //
                historico.setObservacoes(rset.getString("observacoes"));

                //
                historico.setStatus(rset.getString("status"));

                //
                historico.setDataAbertura(rset.getTimestamp("dataAbertura"));

                //
                historico.setDataFechamento(rset.getTimestamp("dataFechamento"));

                //
                listaHistorico.add(historico);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{

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
        return listaHistorico;
    }

    /**
     * Armazena os dados do histórico do banco de dados em uma estrutura de Map
     * @return
     */
    public Map<Integer, Historico> getMapHistorico(){

        String sql = "SELECT * FROM consulta_historico";

        Map<Integer, Historico> mapHistorico = new HashMap<>();

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
            while(rset.next()){

                //
                Historico historico = new Historico();

                //
                historico.setIdHistorico(rset.getInt("idHistorico"));

                //
                historico.setIdChave(rset.getInt("idChave"));

                //
                historico.setNumeroChave(rset.getInt("numerochave"));

                //
                historico.setNome(rset.getString("nome"));

                //
                historico.setCargo(rset.getString("cargo"));

                //
                historico.setObservacoes(rset.getString("observacoes"));

                //
                historico.setStatus(rset.getString("status"));

                //
                historico.setDataAbertura(rset.getTimestamp("dataAbertura"));

                //
                historico.setDataFechamento(rset.getTimestamp("dataFechamento"));

                //
                mapHistorico.putIfAbsent(historico.getIdHistorico(), historico);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{

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
        return mapHistorico;
    }

    /**
     *
     * @param idHistorico
     */
    public void deleteByID(int idHistorico){

        String updateToNULL = "UPDATE historico SET idChave = NULL, idPessoa = NULL, observacoes = NULL, status = NULL, dataAbertura = NULL, dataFechamento = NULL WHERE idHistorico = ?;\n";

        Connection connUpdate = null;

        PreparedStatement pstmUpdate = null;

        try{
            //
            connUpdate = ConnectionFactory.createConnectionToMySQL();

            //
            pstmUpdate = connUpdate.prepareStatement(updateToNULL);

            // ID para fazer o update como null
            pstmUpdate.setInt(1, idHistorico);

            //
            int rowsUpdated = pstmUpdate.executeUpdate();

            if(rowsUpdated > 0) {

                String sql = " DELETE FROM historico WHERE idHistorico = ?;";

                Connection conn = null;

                PreparedStatement pstm = null;

                try{
                    //
                    conn = ConnectionFactory.createConnectionToMySQL();

                    //
                    pstm = conn.prepareStatement(sql);

                    // ID para fazer o update como null
                    pstm.setInt(1, idHistorico);

                    //
                    pstm.execute();

                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    try{
                        //
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
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Não foi possível excluir o registro!\nTente novamente!");
                alert.showAndWait();
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
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

    /**
     *
     * @return
     */
    public List<String> getMeses(){
        String sql = "SELECT * FROM lista_meses";

        List<String> mesesList = new ArrayList<String>();

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
            while(rset.next()){

                //
                String dataFormatada = rset.getString("ano_mes");

                //
                mesesList.add(dataFormatada);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{

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
        return mesesList;
    }

}