package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.dao.PessoaDAO;
import br.com.javafxsecurekey.model.dao.VerifyDAO;
import br.com.javafxsecurekey.model.domain.Pessoa;
import br.com.javafxsecurekey.model.util.TextFieldFormatter;
import br.com.javafxsecurekey.model.validator.CPFValidator;
import br.com.javafxsecurekey.model.validator.EmailValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FXMLPessoasCadastradasController implements Initializable {

    @FXML
    private Button btnEditarDados;
    @FXML
    private TableColumn<?, ?> tc_CPF;
    @FXML
    private TableColumn<?, ?> tc_ativa;
    @FXML
    private TableColumn<?, ?> tc_cargo;
    @FXML
    private TableColumn<?, ?> tc_email;
    @FXML
    private TableColumn<?, ?> tc_empresa;
    @FXML
    private TableColumn<?, ?> tc_nomePessoa;
    @FXML
    private TextField tfPesquisa;
    @FXML
    private TableView<Pessoa> tvPessoasCadastradas;

    private static Pessoa dadosDaPessoaEscolhida = new Pessoa();
    private ObservableList observableMap;
    private Map<Integer, Pessoa> mapPessoas = new HashMap<>();
    private FilteredList<Pessoa> filteredData;
    private static boolean onPopUpScreen = false;

    // -------------------------Tela de Edição de dados Atributes--------------------------
    @FXML
    private Button btnAtualizar;
    @FXML
    private Button btnBloquear;
    @FXML
    private Button btnCancelar;
    @FXML
    private Pane editUserScreen;
    @FXML
    private TextField tfCPF;
    @FXML
    private TextField tfCargo;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfEmpresa;
    @FXML
    private TextField tfNome;
    @FXML
    private TextField tfTelefone;

    void prepararListaTabela() {
        tc_nomePessoa.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tc_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        tc_cargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        tc_empresa.setCellValueFactory(new PropertyValueFactory<>("empresa"));
        tc_CPF.setCellValueFactory(new PropertyValueFactory<>("CPF"));
        tc_ativa.setCellValueFactory(new PropertyValueFactory<>("ativa"));

        // Pegando a lista de viagens do banco
        mapPessoas = PessoaDAO.getMapPessoa();

        // configurando o observable list com os dados da lista do banco
        observableMap = FXCollections.observableArrayList(mapPessoas.values());

        // Criando a filtered list com os dados
        filteredData = new FilteredList<>(observableMap, p -> true);

        // Configurando a tabela após a pesquisa
        tvPessoasCadastradas.setItems(filteredData);

        tfPesquisa.setOnMouseClicked(event -> {
            // Aqui você pode colocar o que quiser que ocorra no clique
            btnEditarDados.setDisable(true);
        });

        // Agora vamos colocar o listener no TextField
        tfPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(pessoa -> {
                // Se o campo de filtro estiver vazio, exibe todos
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Adapte os campos conforme os atributos da sua classe Historico
                if (pessoa.getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (pessoa.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (pessoa.getCargo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (pessoa.getEmpresa().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (pessoa.getCPF().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (pessoa.getAtiva().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false; // Não corresponde ao filtro
            });
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(!onPopUpScreen)
            prepararListaTabela();
        else if(dadosDaPessoaEscolhida != null)
        {
            tfNome.setText(dadosDaPessoaEscolhida.getNome());
            tfCPF.setText(dadosDaPessoaEscolhida.getCPF());
            tfEmail.setText(dadosDaPessoaEscolhida.getEmail());
            tfEmpresa.setText(dadosDaPessoaEscolhida.getEmpresa());
            tfTelefone.setText(dadosDaPessoaEscolhida.getTelefone());
            tfCargo.setText(dadosDaPessoaEscolhida.getCargo());
        }

    }

    /**
     * METODO QUE CHAMA A TELA POP UP
     * @throws IOException
     */
    void abrirTelaPopUp() throws IOException {
        onPopUpScreen = true;
        // Carrega o FXML do pop-up
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafxsecurekey/view/screens/FXMLEditarDadosPessoaScreen.fxml"));
        Parent popupRoot = loader.load();

        // Cria um novo Stage para o pop-up
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);  // Modalidade que bloqueia todas as janelas ate a pop up ser fechada
        popupStage.setResizable(false);  // Impede redimensionamento

        // Define a cena e exibe
        Scene popupScene = new Scene(popupRoot);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();  // Aguarda até o pop-up ser fechado
    }

    @FXML
    void btnEditarDadosOnMouseClicked(MouseEvent event) throws IOException {
        if(tvPessoasCadastradas.getSelectionModel().getSelectedItem() != null)
        {
            if(tvPessoasCadastradas.getSelectionModel().getSelectedItem().isPessoa())
            {

                dadosDaPessoaEscolhida = tvPessoasCadastradas.getSelectionModel().getSelectedItem();

                // Chamar tela de edição de dados de pessoa
                abrirTelaPopUp();
                prepararListaTabela();
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null,"Selecione um registro da tabela corretamente para realizar a edição dos dados!");
        }
        btnEditarDados.setDisable(true);
    }

    @FXML
    void tvPessoasCadastradasOnMouseClicked(MouseEvent event) {
        btnEditarDados.setDisable(false);
    }

    // ----------------Metodos da tela de edição de dados-----------------------
    @FXML
    void btnAtualizarOnMouseClicked(MouseEvent event) {
        // Verifico se todos os campos estão preenchidos
        if(
            !tfEmail.getText().isEmpty() &&
            !tfEmpresa.getText().isEmpty() &&
            !tfCargo.getText().isEmpty() &&
            !tfTelefone.getText().isEmpty()
        ) {
            // Valida o e-mail digitado para saber se é um e-mail válido
            if(EmailValidator.isValidEmail(tfEmail.getText())) {
                // Pergunto se a pessoa deseja mesmo realizar a atualização
                int opcao = JOptionPane.showOptionDialog(null, "Tem certeza que deseja Atualizar os dados de %s ?".formatted(dadosDaPessoaEscolhida.getNome()), "Confirmação final",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, 0);

                // Se o botão sim for apertado, atualizamos os dados da pessoa
                if (opcao == 0) {
                    dadosDaPessoaEscolhida.setEmail(tfEmail.getText());
                    dadosDaPessoaEscolhida.setEmpresa(tfEmpresa.getText());
                    dadosDaPessoaEscolhida.setCargo(tfCargo.getText());
                    dadosDaPessoaEscolhida.setTelefone(tfTelefone.getText());

                    PessoaDAO.setDefaultResult();

                    PessoaDAO.update(dadosDaPessoaEscolhida);

                    if(PessoaDAO.getResult())
                    {
                        Alert a = new Alert(Alert.AlertType.INFORMATION, "Dados atualizados com Sucesso!");
                        a.showAndWait();
                        btnCancelarMouseClicked(null);
                        PessoaDAO.setDefaultResult();
                    }
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "O E-mail digitado é inválido\nPor favor, digite um E-mail válido/correto!",
                        "Erro tentar realizar cadastro", JOptionPane.WARNING_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos, por favor!",
                    "Erro tentar realizar cadastro", JOptionPane.WARNING_MESSAGE);
        }
    }

    @FXML
    void btnBloquearMouseClicked(MouseEvent event) {
        // Pergunto se a pessoa deseja mesmo realizar o cadastro
        int opcao = JOptionPane.showOptionDialog(null, "Tem certeza que deseja BLOQUEAR %s ?\nApós essa ação, a pessoa NÃO poderá pegar chaves emprestadas,\naté que seja DESBLOQUEADA".formatted(dadosDaPessoaEscolhida.getNome()), "Confirmação final",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, 0);

        // Se o botão sim for apertado, bloqueia a pessoa, deixando-a Não ativa
        if (opcao == 0) {
            PessoaDAO.setDefaultResult();

            PessoaDAO.updateAtiva(dadosDaPessoaEscolhida, "Não");

            if(PessoaDAO.getResult())
            {
                Alert a = new Alert(Alert.AlertType.INFORMATION, " %s foi bloqueado com Sucesso!".formatted(dadosDaPessoaEscolhida.getNome()));
                a.showAndWait();
                btnCancelarMouseClicked(null);
                PessoaDAO.setDefaultResult();
            }
        }
    }

    @FXML
    void btnDesbloquearMouseClicked(MouseEvent event) {
        // Pergunto se a pessoa deseja mesmo realizar o cadastro
        int opcao = JOptionPane.showOptionDialog(null, "Tem certeza que deseja DESBLOQUEAR %s ?\nApós essa ação, a pessoa poderá pegar chaves emprestadas novamente".formatted(dadosDaPessoaEscolhida.getNome()), "Confirmação final",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, 0);

        // Se o botão sim for apertado, desbloqueamos a pessoa, colocando-a como ativa
        if (opcao == 0) {
            PessoaDAO.setDefaultResult();

            PessoaDAO.updateAtiva(dadosDaPessoaEscolhida, "Sim");

            if(PessoaDAO.getResult())
            {
                Alert a = new Alert(Alert.AlertType.INFORMATION, " %s foi desbloqueado com Sucesso!".formatted(dadosDaPessoaEscolhida.getNome()));
                a.showAndWait();
                btnCancelarMouseClicked(null);
                PessoaDAO.setDefaultResult();
            }
        }
    }

    @FXML
    void btnCancelarMouseClicked(MouseEvent event) {
        // fechamos o stage da tela Pop Up
        onPopUpScreen = false;
        Stage stageAtual = (Stage) ((Node) (btnCancelar)).getScene().getWindow();
        stageAtual.close();

    }

    @FXML
    void tfCPFKeyReleased(KeyEvent event) {
        TextFieldFormatter tff = new TextFieldFormatter();
        tff.setMask("###.###.###-##");
        tff.setCaracteresValidos("0123456789");
        tff.setTf(tfCPF);
        tff.formatter();
    }

    @FXML
    void tfTelefoneOnKeyReleased(KeyEvent event) {
        TextFieldFormatter tff = new TextFieldFormatter();
        tff.setMask("(##)#####-####");
        tff.setCaracteresValidos("0123456789");
        tff.setTf(tfTelefone);
        tff.formatter();
    }
}