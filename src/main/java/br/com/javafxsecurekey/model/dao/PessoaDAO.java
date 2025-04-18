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
import java.util.LinkedList;

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

        String sql = "INSERT INTO pessoa(nome, cpf, email, telefone, empresa, cargo, dtRegistro) VALUES (?, ?, ?, ?, ?, ?, ?)";

        pessoa.setDtRegistro(new Timestamp(System.currentTimeMillis()));

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
            pstm.setString(3, pessoa.getEmail());
            pstm.setString(4, pessoa.getTelefone());
            pstm.setString(5, pessoa.getEmpresa());
            pstm.setString(6, pessoa.getCargo());
            pstm.setTimestamp(7, pessoa.getDtRegistro());

            //Executa a Query
            pstm.execute();

            result = true;
            Alert sucesso = new Alert(Alert.AlertType.INFORMATION, "Pessoa cadastrada com sucesso!");
            sucesso.showAndWait();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Ocorreru um erro ao tentar cadastrar a pessoa!\nDetalhes: "+e.getMessage(),
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
     * O método executa o SELECT no banco de dados
     */
    public static LinkedList<Pessoa> getPessoa(){

        String sql = "SELECT * FROM pessoa";

        LinkedList<Pessoa> listaPessoa = new LinkedList<>();

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

                //Recupera o email da Pessoa
                pessoa.setEmail(rset.getString("email"));

                //Recupera o telefone da Pessoa
                pessoa.setTelefone(rset.getString("telefone"));

                //Recupera o empresa da Pessoa
                pessoa.setEmpresa(rset.getString("empresa"));

                //Recupera o cargo da Pessoa
                pessoa.setCargo(rset.getString("cargo"));

                //Recupera o data de registro da Pessoa
                pessoa.setDtRegistro(rset.getTimestamp("dtRegistro"));

                //Adiciona a Pessoa com todos os dados registrados à lista de Pessoas
                listaPessoa.add(pessoa);
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

        return listaPessoa;
    }

    /**
     * O método executa o comando UPDATE no banco de dados
     */
    public void update(Pessoa pessoa){

        String sql = "UPDATE pessoa SET nome = ?, cpf = ?, email = ?, telefone = ?, empresa = ?, cargo = ?, DtRegistro = ?"+
                "WHERE idPessoa = ?";

        Connection conn = null;

        PreparedStatement pstm = null;

        try{
            //Cria a conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma query
            pstm = conn.prepareStatement(sql);

            //Adicina os valores para atualizar
            pstm.setString(1, pessoa.getNome());
            pstm.setString(2, pessoa.getCPF());
            pstm.setString(3, pessoa.getEmail());
            pstm.setString(4, pessoa.getTelefone());
            pstm.setString(5, pessoa.getEmpresa());
            pstm.setString(6, pessoa.getCargo());
            pstm.setDate(7, new Date(pessoa.getDtRegistro().getTime()));

            //Qual o ID do registro que deseja atualizar? passando o id de pessoa para atualizar o registro
            pstm.setInt(8, pessoa.getIdPessoa());

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
