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
package br.com.javafxsecurekey.model.domain;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

/**
 * Essa é a Classe model de Login, contendo todos os atributos e métodos sobre Login
 * @author Bruno Sousa da Silva
 */
public class Log implements Comparable<Log> {

    private int idLog;
    private int idUsuario;
    private String username;
    private String role;
    private Timestamp dtLogin;
    private Timestamp dtLogout;

    public int getIdLog() {
        return idLog;
    }

    public void setIdLog(int idLog) {
        this.idLog = idLog;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setUsername(String nome) {
        username = nome;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setDtLogin(Timestamp dtLogin) {
        this.dtLogin = dtLogin;
    }

    public Timestamp getDtLogin() {
        return dtLogin;
    }

    public String getDtLoginFormated() {
        return dtLogin.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy | HH:mm:ss"));
    }

    public void setDtLogout(Timestamp dtLogout) {
        this.dtLogout = dtLogout;
    }

    public Timestamp getDtLogout() {
        return dtLogout;
    }

    public String getDtLogoutFormated() {
        if(dtLogout != null)
            return dtLogout.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy | HH:mm:ss"));
        else
            return null;
    }

    /**
     * O metodo compareTo permite organizar os elementos na estrutura de arvore
     * @param l the object to be compared.
     * @return -1, 1 ou 0 dependendo de é maior, menor ou igual ao elemento comparado
     */
    @Override
    public int compareTo(Log l) {
        if(idLog < l.idLog)
        {
            return -1;
        }
        else if(idLog > l.idLog)
        {
            return 1;
        }
        else
            return 0;
    }
}
