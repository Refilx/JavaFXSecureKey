package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.dao.PessoaDAO;
import br.com.javafxsecurekey.model.dao.UsuarioDAO;
import br.com.javafxsecurekey.model.dao.VerifyDAO;
import br.com.javafxsecurekey.model.domain.Pessoa;
import br.com.javafxsecurekey.model.domain.Usuario;
import br.com.javafxsecurekey.model.util.CaseTextFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class FXMLCadastroUserController implements Initializable {

    @FXML
    private ToggleGroup TipoUsuario;
    @FXML
    private Button btnCadastrar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Pane cadastroUserScreen;
    @FXML
    private ListView<String> lvSelecaoPessoa;
    @FXML
    private PasswordField passFieldConfirmSenha;
    @FXML
    private PasswordField passFieldSenha;
    @FXML
    private RadioButton rb_admin;
    @FXML
    private RadioButton rb_gerente;
    @FXML
    private RadioButton rb_operador;
    @FXML
    private TextField textFieldSelecionaPessoa;
    @FXML
    private TextField textFieldUsername;

    LinkedList<Pessoa> listPessoas = new LinkedList<>();
    LinkedList<String> listAuxPessoa = new LinkedList<>();

    ObservableList<String> obsPessoas;

    private Pessoa pessoaEscolhida = new Pessoa();
    private static Usuario usuario = new Usuario();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CaseTextFormatter.applyLowerCase(textFieldUsername);
        carregarDados();
    }

    @FXML
    void btnCadastrarMouseClicked(MouseEvent event) {
        lvSelecaoPessoa.setVisible(false);
        // Verifico se todos os campos estão preenchidos
        if(
                !textFieldSelecionaPessoa.getText().isEmpty() &&
                !textFieldUsername.getText().isEmpty() &&
                !passFieldSenha.getText().isEmpty() &&
                !passFieldConfirmSenha.getText().isEmpty() &&
                (rb_admin.isSelected() || rb_gerente.isSelected() || rb_operador.isSelected())
        ) {
            // Verifica se o campo senha e comfimação de senha são iguais
            if(passFieldSenha.getText().equals(passFieldConfirmSenha.getText())) {

                // Verifica se já existe alguma pessoa usuária cadastrada com esse username no banco de dados
                if (new VerifyDAO().verifyUsername(textFieldUsername.getText())) {

                    // Pergunto se a pessoa deseja mesmo realizar o cadastro
                    int opcao = JOptionPane.showOptionDialog(null, "Tem certeza que deseja cadastrar um novo usuário?", "Confirmação final",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, 0);

                    // Se o botão sim for apertado, cadastramos o novo usuário
                    if (opcao == 0) {

                        usuario.setIdPessoa(pessoaEscolhida.getIdPessoa());
                        usuario.setUsername(textFieldUsername.getText());
                        usuario.setPassword(passFieldSenha.getText());
                        if (rb_operador.isSelected())
                            usuario.setRole("Operador");
                        else if (rb_gerente.isSelected())
                            usuario.setRole("Gerente");
                        else if (rb_admin.isSelected())
                            usuario.setRole("Administrador");

                        UsuarioDAO.save(usuario);

                        if(UsuarioDAO.getResult())
                        {
                            btnCancelarMouseClicked(null);
                            UsuarioDAO.setDefaultResult();
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "O usuário NÃO foi cadastrado!",
                                "Cancelamento do cadastro do usuário", JOptionPane.INFORMATION_MESSAGE);
                        btnCancelarMouseClicked(null);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Já existe um usuário com esse username cadastrado\n Digite um username diferente, por favor",
                            "Erro tentar realizar cadastro", JOptionPane.WARNING_MESSAGE);
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "As senhas estão diferentes!",
                        "Erro tentar realizar cadastro", JOptionPane.WARNING_MESSAGE);
                passFieldSenha.setText(null);
                passFieldConfirmSenha.setText(null);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos, por favor!",
                    "Erro tentar realizar cadastro", JOptionPane.WARNING_MESSAGE);
        }
    }

    @FXML
    void btnCancelarMouseClicked(MouseEvent event) {
        textFieldSelecionaPessoa.setText(null);
        textFieldUsername.setText(null);
        passFieldSenha.setText(null);
        passFieldConfirmSenha.setText(null);
        rb_admin.setSelected(false);
        rb_gerente.setSelected(false);
        rb_operador.setSelected(false);
    }

    @FXML
    void pfConfirmSenhaOnMouseClicked(MouseEvent event) {
        lvSelecaoPessoa.setVisible(false);
    }

    @FXML
    void pfSenhaOnMouseClicked(MouseEvent event) {
        lvSelecaoPessoa.setVisible(false);
    }

    @FXML
    void tfSelecionePessoaOnMouseClicked(MouseEvent event) {
        lvSelecaoPessoa.setVisible(true);
    }

    @FXML
    void tfUsernameOnMouseClicked(MouseEvent event) {
        lvSelecaoPessoa.setVisible(false);
    }

    private void carregarDados() {
        // Listas principais recebem os dados do banco
        listPessoas = PessoaDAO.getPessoa();

        // Passando alguns dados específicos para listas auxiliares que exibirão os dados na ListView
        for(Pessoa p : listPessoas)
            listAuxPessoa.add(p.getNome()+" - "+p.getCPF().substring(0, 3)+".***.***-"+p.getCPF().substring(12, 14)); // formatando a exibição dos dados: Nome da pessoa + CPF: 000.***.***-00

        // Carregamos as listas auxiliares com os dados exibíveis
        obsPessoas = FXCollections.observableArrayList(listAuxPessoa);

        // FilteredLists para filtragem dinâmica
        FilteredList<String> filteredPessoas = new FilteredList<>(obsPessoas, p -> true);

        // Associa as listas filtradas às ListViews
        lvSelecaoPessoa.setItems(filteredPessoas);

        // Listener para o filtro dinâmico conforme o usuário digita no TextField
        textFieldSelecionaPessoa.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredPessoas.setPredicate(item -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lower = newVal.toLowerCase();
                return item.toLowerCase().contains(lower);
            });
        });

        // Preenchendo o TextField ao clicar em um item da ListView
        lvSelecaoPessoa.setOnMouseClicked(e -> {
            String selected = lvSelecaoPessoa.getSelectionModel().getSelectedItem();

            for(Pessoa p : listPessoas) // Procurando o item na lista, se for encontrado, armazenamos os dados da pessoa escolhida
            {
                if((p.getNome()+" - "+p.getCPF().substring(0, 3)+".***.***-"+p.getCPF().substring(12, 14)).equals(selected)) // Comparando a formatação do item da vez com o item selecionado
                {
                    pessoaEscolhida = p;
                    break;
                }
            }

            if (selected != null) {
                textFieldSelecionaPessoa.setText(selected);
                lvSelecaoPessoa.setVisible(false);
            }
        });

    }
}
