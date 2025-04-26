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

/**
 * Essa é a Classe model de chaves, contendo todos os atributos e métodos sobre chaves
 * @author Bruno Sousa da Silva
 */
public class Chave implements Comparable<Chave> {

    private int idChave;
    private int numeroChave;
    private String sala;
    private String bloco_predio;
    private String observacoes;
    private int quantChave;
    private String status;
    private String possuiReserva;

    public int getIdChave() {
        return idChave;
    }

    public void setIdChave(int id) {
        idChave = id;
    }

    public void setNumeroChave(int numeroChave) {
        this.numeroChave = numeroChave;
    }

    public int getNumeroChave() {
        return numeroChave;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getSala() {
        return sala;
    }

    public void setBloco_predio(String bloco_predio) {
        this.bloco_predio = bloco_predio;
    }

    public String getBloco_predio() {
        return bloco_predio;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public void setQuantChave(int quant_chave) {
        this.quantChave = quant_chave;
    }

    public int getQuantChave() {
        return quantChave;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setPossuiReserva(String possuiReserva) {
        this.possuiReserva = possuiReserva;
    }

    public String getPossuiReserva() {
        return possuiReserva;
    }

    /**
     * O metodo compareTo permite organizar os elementos na estrutura de arvore
     * @param c the object to be compared.
     * @return -1, 1 ou 0 dependendo de é maior, menor ou igual ao elemento comparado
     */
    @Override
    public int compareTo(Chave c) {
        if(idChave < c.idChave)
        {
            return -1;
        }
        else if(idChave > c.idChave)
        {
            return 1;
        }
        else
            return 0;
    }
}
