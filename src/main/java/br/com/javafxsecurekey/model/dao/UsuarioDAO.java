/*
 * The MIT License
 *
 * Copyright 2024 Adiamel Santos da Silva, Bruno Sousa da Silva.
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
import br.com.javafxsecurekey.model.domain.Usuario;
import br.com.javafxsecurekey.model.util.Arvore;
import javafx.scene.control.Alert;
import org.jasypt.util.password.BasicPasswordEncryptor;

import javax.swing.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Essa Classe faz a manipulação dos dados sobre usuários com o banco de dados
 * @author Bruno Sousa da Silva
 */
public class UsuarioDAO {

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
    public static void save(Usuario usuario){

        String sql = "INSERT INTO usuario(username, password, role, dtRegistro, idPessoa) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;

        PreparedStatement pstm = null;

        result = false;

        try{
            //Cria uma conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma Query
            pstm = conn.prepareStatement(sql);

            //Iniciamos a classe que fará a criptografia da senha do usuário antes de salva-la no banco
            BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();

            //Neste ponto chamamos a função que criptografa a senha e passamos no parametro a senha armazenada no usuário
            String senhaCriptografada = passwordEncryptor.encryptPassword(usuario.getPassword());

            //Adicionar valores que são esperados pela Query
            pstm.setString(1, usuario.getUsername());
            pstm.setString(2, senhaCriptografada);
            pstm.setString(3, usuario.getRole());
            pstm.setDate(4, new Date(usuario.getDtRegistro().getTime()));
            pstm.setInt(5, usuario.getIdPessoa());

            //Executa a Query
            pstm.execute();

            result = true;
            Alert sucesso = new Alert(Alert.AlertType.INFORMATION, "Usuário cadastrado com sucesso!");
            sucesso.showAndWait();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao tentar salvar o usuário!\nDetalhes: "+e.getMessage(),
                    "Erro de Cadastro", JOptionPane.WARNING_MESSAGE);
        }finally {

            try{
                //Fecha as conexões que foram abertas com o banco de dados
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
    public List<Usuario> getUsuario(){

        String sql = "SELECT * FROM usuario";

        List<Usuario> listaUsuario = new LinkedList<>();

        Connection conn = null;

        PreparedStatement pstm = null;

        //Classe que vai recuperar os dados do banco. ***SELECT***
        ResultSet rset = null;

        try{
            //Cria uma conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            //Cria uma PreparedStatement para executar uma Query
            pstm = conn.prepareStatement(sql);

            rset = pstm.executeQuery();

            //Enquanto houver um próximo dado para ser armazenado pelo ResultSet, os comandos serão executados
            while(rset.next()){
                Usuario usuario = new Usuario();

                //Recupera o idUser do usuário no banco de dados
                usuario.setIdUsuario(rset.getInt("idUsuario"));

                //Recupera o idPessoa do usuário no banco de dados
                usuario.setIdPessoa(rset.getInt("idPessoa"));

                //Recupera o username do usuário no banco de dados
                usuario.setUsername(rset.getString("username"));

                //Recupera a password do usuário no banco de dados
                usuario.setPassword(rset.getString("password"));

                //Recupera a role do usuário no banco de dados
                usuario.setRole(rset.getString("role"));

                //Recupera a data de registro do usuário no banco de dados
                usuario.setDtRegistro(rset.getTimestamp("dtRegistro"));

                //Adiciona o Usuário com todos os dados registrados à lista de Usuários
                listaUsuario.add(usuario);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {

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

        return listaUsuario;
    }

    /**
     * O método executa o SELECT no banco de dados e armazena os dados em árvore
     */
    public Arvore<Usuario> getUsuarioEmArvore(){

        String sql = "SELECT * FROM usuario";

        Arvore<Usuario> arvoreUsuario = new Arvore<>();

        Connection conn = null;

        PreparedStatement pstm = null;

        //Classe que vai recuperar os dados do banco. ***SELECT***
        ResultSet rset = null;

        try{
            //Cria uma conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            //Cria uma PreparedStatement para executar uma Query
            pstm = conn.prepareStatement(sql);

            rset = pstm.executeQuery();

            //Enquanto houver um próximo dado para ser armazenado pelo ResultSet, os comandos serão executados
            while(rset.next()){
                Usuario usuario = new Usuario();

                //Recupera o idUser do usuário no banco de dados
                usuario.setIdUsuario(rset.getInt("idUsuario"));

                //Recupera o idPessoa do usuário no banco de dados
                usuario.setIdPessoa(rset.getInt("idPessoa"));

                //Recupera o username do usuário no banco de dados
                usuario.setUsername(rset.getString("username"));

                //Recupera a password do usuário no banco de dados
                usuario.setPassword(rset.getString("password"));

                //Recupera a role do usuário no banco de dados
                usuario.setRole(rset.getString("role"));

                //Recupera a data de registro do usuário no banco de dados
                usuario.setDtRegistro(rset.getTimestamp("dtRegistro"));

                //Adiciona o Usuário com todos os dados registrados à lista de Usuários
                arvoreUsuario.adicionar(usuario);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {

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

        return arvoreUsuario;
    }

    /**
     * O método executa o UPDATE no banco de dados
     * @param usuario
     */
    public void update(Usuario usuario){

        String sql = "UPDATE usuario SET username = ?, password = ?, role = ?, dtRegistro = ?" +
                     "WHERE idUsuario = ?";

        Connection conn = null;

        PreparedStatement pstm = null;

        try{
            //Cria uma conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma Query
            pstm = conn.prepareStatement(sql);

            //Iniciamos a classe que fará a criptografia da senha do usuário antes de salva-la no banco
            BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();

            //Neste ponto chamamos a função que criptografa a senha e passamos no parametro a senha armazenada no usuário
            String senhaCriptografada = passwordEncryptor.encryptPassword(usuario.getPassword());

            //Passando os valores esperados pela Query
            pstm.setString(1, usuario.getUsername());
            pstm.setString(2, senhaCriptografada);
            pstm.setString(3, usuario.getRole());
            pstm.setDate(4, new Date(usuario.getDtRegistro().getTime()));

            // Passando o id da linha que será atualizada
            pstm.setInt(5, usuario.getIdUsuario());

            //Executando a query
            pstm.execute();

        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            try{
                //Fechando as conexões abertas
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
     * Busco o id do usuário no banco de dados usando o email para buscá-lo
     * @param usuario
     * @return usuario contendo o Id do usuário específico com o e-mail especificado
     * @author Bruno Sousa da Silva
     */
    public static void getIdUsuarioByEmail(Usuario usuario) {
        String sql = "SELECT U.idUsuario, P.email FROM pessoa P\n" +
                "JOIN usuario U ON (P.idPessoa = U.idPessoa)\n" +
                "WHERE email = ?";

        Connection conn =  null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = ConnectionFactory.createConnectionToMySQL();

            pstm = conn.prepareStatement(sql);

            pstm.setString(1, usuario.getEmail());

            rset = pstm.executeQuery();

            if(rset.next()) {
                usuario.setIdUsuario(rset.getInt("idUsuario"));
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
    }


    /**
     * Atualiza a senha do usuário específico após o processo de redefinição de senha
     * @param usuario
     * @author Bruno Sousa da Silva
     */
    public static void updateSenhaDoUsuario(Usuario usuario) {
        String sql = "UPDATE usuario SET password = ?\n" +
                "WHERE idUsuario = ?";

        Connection conn =  null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = ConnectionFactory.createConnectionToMySQL();

            pstm = conn.prepareStatement(sql);

            // Criptografando a nova senha do usuário antes de salvá-la no banco
            BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
            usuario.setPassword(passwordEncryptor.encryptPassword(usuario.getPassword()));

            pstm.setString(1, usuario.getPassword());
            pstm.setInt(2, usuario.getIdUsuario());

            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Senha Atualizada com Sucesso!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Erro ao realizar a atualização da senha!");
                alert.showAndWait();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (conn != null)
                    conn.close();

                if (pstm != null)
                    pstm.close();

                if (rset != null)
                    rset.close();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * O método executa o UPDATE no banco de dados
     * @param idUser
     */
    public void deleteByID(int idUser){

        String sql = "DELETE FROM usuario WHERE idUser = ?";

        Connection conn = null;

        PreparedStatement pstm = null;

        try{
            //Cria uma conexão com o banco de dados
            conn = ConnectionFactory.createConnectionToMySQL();

            //Criamos uma PreparedStatement para executar uma Query
            pstm = conn.prepareStatement(sql);

            //Passando o id do usuario que será excluído
            pstm.setInt(1, idUser);

            //Executa a Query
            pstm.execute();

        }catch(Exception e){
            e.printStackTrace();
        }finally {
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
