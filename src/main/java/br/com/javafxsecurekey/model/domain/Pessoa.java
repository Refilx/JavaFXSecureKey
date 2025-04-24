/*
 * The MIT License
 *
 * Copyright 2025 Bruno Sousa da Silva.
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

/**
 * Essa é a Classe model de Pessoa, contendo todos os atributos e métodos sobre Pessoa
 * @author Bruno Sousa da Silva
 */
public class Pessoa implements Comparable<Pessoa> {

    private int idPessoa;
    private String nome;
    private String CPF;
    private String email;
    private String telefone;
    private String empresa;
    private String cargo;
    private Timestamp dtRegistro;

    public void setIdPessoa(int id){
        idPessoa = id;
    }

    public int getIdPessoa(){
        return idPessoa;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setCPF(String cpf) {
        this.CPF = cpf;
    }

    public String getCPF() {
        return CPF;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setDtRegistro(Timestamp dtRegistro) {
        this.dtRegistro = dtRegistro;
    }

    public Timestamp getDtRegistro() {
        return dtRegistro;
    }

    /**
     * O metodo compareTo permite organizar os elementos na estrutura de arvore
     * @param p the object to be compared.
     * @return -1, 1 ou 0 dependendo de é maior, menor ou igual ao elemento comparado
     */
    @Override
    public int compareTo(Pessoa p) {
        if(this.idPessoa < p.idPessoa)
        {
            return -1;
        }
        else if(this.idPessoa > p.idPessoa)
        {
            return 1;
        }
        else
            return 0;
    }
}
