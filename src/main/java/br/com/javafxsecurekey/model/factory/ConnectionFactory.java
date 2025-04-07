package br.com.javafxsecurekey.model.factory;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Essa Classe estabelece a conexão com o banco de dados MySQL
 * @author Bruno Sousa
 */
public class ConnectionFactory {

    //Nome do usuário do mysql
    private static final String USERNAME = "root";

    //Senha do banco
    private static final String PASSWORD = "1234";

    //Caminho do banco de dados, porta
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/javafxsecurekey";

    /*
    Conexão com o banco de dados
     */
    public static Connection createConnectionToMySQL() throws Exception {

        // Cria conexão com o banco de dados
        Connection connection = DriverManager.getConnection(DATABASE_URL,USERNAME,PASSWORD);

        return connection;
    }

    public static void main(String[] args) throws Exception {

        //Recuperar uma conexão com o banco de dados
        Connection conn = createConnectionToMySQL();

        //Testar se a conexão é nula
        if(conn != null) {
            System.out.println("Conexão Obtida com Sucesso!");
            conn.close();
        }

    }
}
