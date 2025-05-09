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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Essa é a Classe model do histórico de empréstimo de chaves, contendo todos os atributos e métodos sobre o histórico de empréstimo de chaves
 * @author Bruno Sousa da Silva
 */
public class Historico implements Comparable<Historico> {

    private int idHistorico;
    private String nome; //Guarda o valor apenas na aplicação
    private String cargo; //Guarda o valor apenas na aplicação
    private int numeroChave; //Guarda o valor apenas na aplicação
    private int idChave;
    private int idPessoa;
    private String observacoes;
    private String status;
    private Timestamp dataAbertura;
    private Timestamp dataFechamento;

    public int getIdHistorico() {
        return idHistorico;
    }

    public void setIdHistorico(int id) {
        idHistorico = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public int getNumeroChave() {
        return numeroChave;
    }

    public void setNumeroChave(int numeroChave) {
        this.numeroChave = numeroChave;
    }

    public void setIdChave(int idChave) {
        this.idChave = idChave;
    }

    public int getIdChave() {
        return idChave;
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setDataAbertura(Timestamp dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Timestamp getDataAbertura() {
        return dataAbertura;
    }

    public String getDataAberturaFormated() {
        return dataAbertura.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy | HH:mm:ss"));
    }

    public void setDataFechamento(Timestamp dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public Timestamp getDataFechamento() {
        return dataFechamento;
    }

    public String getDataFechamentoFormated() {
        if(dataFechamento != null)
            return dataFechamento.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy | HH:mm:ss"));
        else
            return null;
    }

    /**
     * O metodo compareTo permite organizar os elementos na estrutura de arvore
     * @param h the object to be compared.
     * @return -1, 1 ou 0 dependendo de é maior, menor ou igual ao elemento comparado
     */
    @Override
    public int compareTo(Historico h) {
        if(idHistorico < h.idHistorico)
        {
            return -1;
        }
        else if(idHistorico > h.idHistorico)
        {
            return 1;
        }
        else
            return 0;
    }
}
