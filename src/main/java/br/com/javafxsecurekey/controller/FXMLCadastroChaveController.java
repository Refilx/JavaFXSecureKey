package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.dao.ChaveDAO;
import br.com.javafxsecurekey.model.domain.Chave;
import br.com.javafxsecurekey.model.util.CaseTextFormatter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLCadastroChaveController implements Initializable {

    @FXML
    private ToggleGroup PossuiChaveReserva;

    @FXML
    private Button btnCadastrar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Pane cadastroUserScreen;

    @FXML
    private RadioButton rb_nao;

    @FXML
    private RadioButton rb_sim;

    @FXML
    private TextArea taDescricao;

    @FXML
    private TextField tfBloco;

    @FXML
    private TextField tfNomeSala;

    @FXML
    private TextField tfNumeroChave;

    private Chave chave = new Chave();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CaseTextFormatter.applyUpperCase(tfNomeSala);
        CaseTextFormatter.applyUpperCase(tfBloco);
        tfNomeSala.setText("");
        tfNumeroChave.setText("");
        tfBloco.setText("");
        taDescricao.setText("");
    }

    @FXML
    void btnCadastrarMouseClicked(MouseEvent event) {
        // Verifico se todos os campos estão preenchidos
        if(
            !tfNumeroChave.getText().isEmpty() &&
            !tfNomeSala.getText().isEmpty() &&
            !tfBloco.getText().isEmpty() &&
            (rb_sim.isSelected() || rb_nao.isSelected())
        )
        {
            boolean conseguiu = true;
            try {
                chave.setNumeroChave(Integer.parseInt(tfNumeroChave.getText()));
            } catch (NumberFormatException e) {
                conseguiu = false;
            }

            if(conseguiu) {
                chave.setSala(tfNomeSala.getText());
                chave.setBloco_predio(tfBloco.getText());
                chave.setObservacoes(taDescricao.getText());

                if (rb_sim.isSelected()) {
                    chave.setQuantChave(2);
                    chave.setPossuiReserva("Sim");
                } else if (rb_nao.isSelected()) {
                    chave.setQuantChave(1);
                    chave.setPossuiReserva("Não");
                }

                chave.setStatus("DISPONÍVEL");

                ChaveDAO.save(chave);

                btnCancelarMouseClicked(null);
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.WARNING, "O campo de número da chave está errado!\nUtilize apenas números");
                alert.showAndWait();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Preencha todos os campos, por favor!");
            alert.showAndWait();
        }
    }

    @FXML
    void btnCancelarMouseClicked(MouseEvent event) {
        tfNumeroChave.setText(null);
        tfNomeSala.setText(null);
        tfBloco.setText(null);
        taDescricao.setText(null);
        rb_sim.setSelected(false);
        rb_nao.setSelected(false);
    }
}
