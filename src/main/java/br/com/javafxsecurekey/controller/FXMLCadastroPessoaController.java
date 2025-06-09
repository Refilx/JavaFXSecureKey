package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.dao.ChaveDAO;
import br.com.javafxsecurekey.model.dao.PessoaDAO;
import br.com.javafxsecurekey.model.dao.VerifyDAO;
import br.com.javafxsecurekey.model.domain.Chave;
import br.com.javafxsecurekey.model.domain.Pessoa;
import br.com.javafxsecurekey.model.util.CaseTextFormatter;
import br.com.javafxsecurekey.model.util.TextFieldFormatter;
import br.com.javafxsecurekey.model.validator.CPFValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FXMLCadastroPessoaController implements Initializable {

    @FXML
    private Button btnCadastrar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnToLeft;
    @FXML
    private Button btnToRight;
    @FXML
    private Pane cadastroUserScreen;
    @FXML
    private ListView<String> leftList;
    @FXML
    private ListView<String> rightList;
    @FXML
    private TextField tfCPF;
    @FXML
    private TextField tfCargo;
    @FXML
    private TextField tfNome;

    private ObservableList<String> leftItems;
    private ObservableList<String> rightItems;
    private Map<Integer, Chave> mapChaves = new HashMap<>();
    private Map<String, Integer> mapLvLeftChaveValues = new HashMap<>();
    private Map<String, Integer> mapLvRightChaveValues = new HashMap<>();
    private Pessoa pessoa = new Pessoa();

    private void carregarDadosNasLVdeChaves() {
        // Listas principais recebem os dados do banco
        mapChaves = ChaveDAO.getMapChave();

        for(Map.Entry<Integer, Chave> mapEntry : mapChaves.entrySet())
            mapLvLeftChaveValues.putIfAbsent("Chave: "+mapEntry.getValue().getNumeroChave()+" - Sala: "+mapEntry.getValue().getSala(), mapEntry.getKey());

        // Carregamos as listas auxiliares com os dados exibíveis
        leftItems = FXCollections.observableArrayList(mapLvLeftChaveValues.keySet());
        rightItems = FXCollections.observableArrayList(mapLvRightChaveValues.keySet());

        // Associa as listas observaveis às ListViews
        leftList.setItems(leftItems);
        rightList.setItems(rightItems);

        // Ação para mover item selecionado para a direita
        btnToRight.setOnAction(e -> {
            String selected = leftList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                leftItems.remove(selected);
                rightItems.add(selected);
                mapLvRightChaveValues.putIfAbsent(selected, mapLvLeftChaveValues.get(selected));
                pessoa.getChavesPermitidas().add(mapChaves.get(mapLvLeftChaveValues.get(selected)).getIdChave());
                mapLvLeftChaveValues.remove(selected);
            }
        });

        // Ação para mover item selecionado para a esquerda
        btnToLeft.setOnAction(e -> {
            String selected = rightList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                rightItems.remove(selected);
                leftItems.add(selected);
                mapLvLeftChaveValues.putIfAbsent(selected, mapLvRightChaveValues.get(selected));
                pessoa.getChavesPermitidas().remove((Object) mapChaves.get(mapLvRightChaveValues.get(selected)).getIdChave());
                mapLvRightChaveValues.remove(selected);
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CaseTextFormatter.applyUpperCase(tfCargo);
        carregarDadosNasLVdeChaves();
    }

    @FXML
    void btnCadastrarMouseClicked(MouseEvent event) {
        // Verifico se todos os campos estão preenchidos
        if(
                !tfNome.getText().isEmpty() &&
                !tfCPF.getText().isEmpty() &&
                !tfCargo.getText().isEmpty()
        ) {
            // Valida se o CPF digitado é um CPF válido/real
            if (CPFValidator.validateCPF(tfCPF.getText())) {

                // Verifica se já existe alguma pessoa usuária cadastrada com esse CPF no banco de dados
                if (VerifyDAO.verifyCPF(tfCPF.getText())) {
                    // Pergunto se a pessoa deseja mesmo realizar o cadastro
                    int opcao = JOptionPane.showOptionDialog(null, "Tem certeza que deseja cadastrar uma nova pessoa?", "Confirmação final",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, 0);

                    // Se o botão sim for apertado, cadastramos a nova pessoa
                    if (opcao == 0) {
                        pessoa.setNome(tfNome.getText());
                        pessoa.setCargo(tfCargo.getText());
                        pessoa.setCPF(tfCPF.getText());

                        PessoaDAO.save(pessoa);

                        if(PessoaDAO.getResult())
                        {
                            tfNome.setText(null);
                            tfCargo.setText(null);
                            tfCPF.setText(null);
                            PessoaDAO.setDefaultResult();
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "A pessoa ainda NÃO foi cadastrada!",
                                "Erro tentar realizar cadastro", JOptionPane.WARNING_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Já existe uma pessoa cadastrada com esse CPF!",
                            "Erro tentar realizar cadastro", JOptionPane.WARNING_MESSAGE);
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "O CPF digitado é inválido\nPor favor, digite um CPF válido/correto!",
                        "Erro tentar realizar cadastro", JOptionPane.WARNING_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos, por favor!",
                    "Erro tentar realizar cadastro", JOptionPane.WARNING_MESSAGE);
        }
    }

    @FXML
    void btnCancelarMouseClicked(MouseEvent event) {
        tfNome.setText(null);
        tfCargo.setText(null);
        tfCPF.setText(null);
    }

    @FXML
    void tfCPFKeyReleased(KeyEvent event) {
        TextFieldFormatter tff = new TextFieldFormatter();
        tff.setMask("###.###.###-##");
        tff.setCaracteresValidos("0123456789");
        tff.setTf(tfCPF);
        tff.formatter();
    }
}
