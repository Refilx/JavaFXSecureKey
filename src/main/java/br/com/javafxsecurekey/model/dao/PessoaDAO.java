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
import br.com.javafxsecurekey.model.domain.Pessoa;
import javafx.scene.control.Alert;

import javax.swing.*;
import java.sql.*;
import java.util.*;

/**
 * Essa Classe faz a manipulação dos dados sobre pessoas com o banco de dados
 * @author Bruno Sousa da Silva
 */
public class PessoaDAO {

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
    public static void save(Pessoa pessoa){

        String sql = "INSERT INTO pessoa(nome, cpf, cargo, chavesPermitidas, dtRegistro, ativa) VALUES (?, ?, ?, ?, ?, ?)";

        pessoa.setDtRegistro(new Timestamp(System.currentTimeMillis()));
        pessoa.setAtiva("Sim");

        // Converter para List<String>
        List<String> chavesPermitidasToString = pessoa.getChavesPermitidas().stream()
                .map(String::valueOf)
                .toList();

        Connection conn = null;

        PreparedStatement pstm = null;

        result = false;

        try{
            //Cria conexão com o banco
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma query
            pstm = conn.prepareStatement(sql);

            //Adicionar os valores que são esperados pela Query
            pstm.setString(1, pessoa.getNome());
            pstm.setString(2, pessoa.getCPF());
            pstm.setString(3, pessoa.getCargo());
            pstm.setString(4, String.join(",", chavesPermitidasToString));
            pstm.setTimestamp(5, pessoa.getDtRegistro());
            pstm.setString(6, pessoa.getAtiva());

            //Executa a Query
            pstm.execute();

            result = true;
            Alert sucesso = new Alert(Alert.AlertType.INFORMATION, "Pessoa cadastrada com sucesso!");
            sucesso.showAndWait();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Ocorreu um erro ao tentar cadastrar a pessoa!\nDetalhes: "+e.getMessage(),
                    "Erro de Cadastro", JOptionPane.WARNING_MESSAGE);
        }finally{

            //Fecha as conexões que foram abertas com o banco de dados
            try{
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
     * O método executa o SELECT no banco de dados e armazena os dados de Map
     */
    public static Map<Integer, Pessoa> getMapPessoa(){

        String sql = "SELECT * FROM pessoa";

        Map<Integer, Pessoa> mapPessoa = new HashMap<>();

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
                Pessoa pessoa = new Pessoa();

                //Recupera o id da Pessoa
                pessoa.setIdPessoa(rset.getInt("idPessoa"));

                //Recupera o nome da Pessoa
                pessoa.setNome(rset.getString("nome"));

                //Recupera o cpf da Pessoa
                pessoa.setCPF(rset.getString("cpf"));

                //Recupera o cargo da Pessoa
                pessoa.setCargo(rset.getString("cargo"));

                // Recupera a lista de chaves que a pessoa está permitida a pegar no formato de string,
                // depois convertemos cadas valor para int e colocamos na lista da pessoa
                LinkedList<String> list = new LinkedList<>(Arrays.asList(rset.getString("chavesPermitidas").split(",")));

                for(String num : list) {
                    if (!num.isEmpty())
                        pessoa.getChavesPermitidas().add(Integer.parseInt(num));
                }

                //Recupera o data de registro da Pessoa
                pessoa.setDtRegistro(rset.getTimestamp("dtRegistro"));

                //Recupera atributo ativa da Pessoa
                pessoa.setAtiva(rset.getString("ativa"));

                pessoa.setCPF(pessoa.getCPF().substring(0, 3)+".***.***-"+pessoa.getCPF().substring(12, 14));

                //Adiciona a Pessoa com todos os dados registrados à lista de Pessoas
                mapPessoa.putIfAbsent(pessoa.getIdPessoa(), pessoa);
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

        return mapPessoa;
    }

    /**
     * O metodo busca todas as pessoas cadastradas e que estejam ativas no BD
     */
    public static Map<Integer, Pessoa> getMapPessoaAtiva(){

        String sql = "SELECT * FROM pessoa WHERE ativa = 'Sim';";

        Map<Integer, Pessoa> mapPessoa = new HashMap<>();

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
                Pessoa pessoa = new Pessoa();

                //Recupera o id da Pessoa
                pessoa.setIdPessoa(rset.getInt("idPessoa"));

                //Recupera o nome da Pessoa
                pessoa.setNome(rset.getString("nome"));

                //Recupera o cpf da Pessoa
                pessoa.setCPF(rset.getString("cpf"));

                // Recupera a lista de chaves que a pessoa está permitida a pegar no formato de string,
                // depois convertemos cadas valor para int e colocamos na lista da pessoa
                LinkedList<String> list = new LinkedList<>(Arrays.asList(rset.getString("chavesPermitidas").split(",")));

                for(String num : list) {
                    if (!num.isEmpty())
                        pessoa.getChavesPermitidas().add(Integer.parseInt(num));
                }

                //Recupera o cargo da Pessoa
                pessoa.setCargo(rset.getString("cargo"));

                //Recupera o data de registro da Pessoa
                pessoa.setDtRegistro(rset.getTimestamp("dtRegistro"));

                if(!pessoa.getCPF().isEmpty())
                    pessoa.setCPF(pessoa.getCPF().substring(0, 3)+".***.***-"+pessoa.getCPF().substring(12, 14));

                //Adiciona a Pessoa com todos os dados registrados à lista de Pessoas
                mapPessoa.putIfAbsent(pessoa.getIdPessoa(), pessoa);
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

        return mapPessoa;
    }

    /**
     * O método executa o comando UPDATE no banco de dados
     */
    public static void update(Pessoa pessoa){

        String sql = "UPDATE pessoa SET chavesPermitidas = ?"+
                "WHERE idPessoa = ?";

        // Converter para List<String>
        List<String> chavesPermitidasToString = pessoa.getChavesPermitidas().stream()
                .map(String::valueOf)
                .toList();

        Connection conn = null;

        PreparedStatement pstm = null;

        try{
            //Cria a conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma query
            pstm = conn.prepareStatement(sql);

            //Adicina os valores para atualizar
            pstm.setString(1, String.join(",", chavesPermitidasToString));

            //Qual o ID do registro que deseja atualizar? passando o id de pessoa para atualizar o registro
            pstm.setInt(2, pessoa.getIdPessoa());

            //Executa a Query
            int rowsUpdated = pstm.executeUpdate();

            if(rowsUpdated > 0)
            {
                result = true;
            }

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
     * Metodo faz o update da pessoa se ela está ativa ou não, para melhor controle
     * @param pessoa
     */
    public static void updateAtiva(Pessoa pessoa, String ativa){

        String sql = "UPDATE pessoa SET ativa = ?"+
                "WHERE idPessoa = ?";

        Connection conn = null;

        PreparedStatement pstm = null;

        try{
            //Cria a conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma query
            pstm = conn.prepareStatement(sql);

            //Adicina os valores para atualizar
            pstm.setString(1, ativa);

            //Qual o ID do registro que deseja atualizar? passando o id de pessoa para atualizar o registro
            pstm.setInt(2, pessoa.getIdPessoa());

            //Executa a Query
            int rowUpdated = pstm.executeUpdate();

            if(rowUpdated > 0)
            {
                result = true;
            }
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
    public void deleteByID(int idPessoa){

        String sql = "DELETE FROM pessoa WHERE idPessoa = ?";

        Connection conn = null;

        PreparedStatement pstm = null;

        try{
            //Cria a conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma query
            pstm = conn.prepareStatement(sql);

            //Passando o id do registro que será excluído
            pstm.setInt(1, idPessoa);

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

}
