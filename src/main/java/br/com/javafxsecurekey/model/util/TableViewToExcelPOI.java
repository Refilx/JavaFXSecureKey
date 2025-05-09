package br.com.javafxsecurekey.model.util;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TableViewToExcelPOI<T> {

    public void exportToExcel(TableView<T> tableView, Window parentWindow) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar como Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo Excel", "*.xlsx"));
        File file = fileChooser.showSaveDialog(parentWindow);

        if (file != null) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Exportação");
                int rowIndex = 0;

                // Criar estilo para o cabeçalho
                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);

                // Escrever cabeçalhos das colunas
                Row headerRow = sheet.createRow(rowIndex++);
                int colIndex = 0;
                for (TableColumn<T, ?> column : tableView.getColumns()) {
                    Cell cell = headerRow.createCell(colIndex++);
                    cell.setCellValue(column.getText());
                    cell.setCellStyle(headerStyle);
                }

                // Escrever dados
                ObservableList<T> items = tableView.getItems();
                for (T item : items) {
                    Row dataRow = sheet.createRow(rowIndex++);
                    colIndex = 0;
                    for (TableColumn<T, ?> column : tableView.getColumns()) {
                        Object cellData = column.getCellData(item);
                        Cell cell = dataRow.createCell(colIndex++);
                        cell.setCellValue(cellData != null ? cellData.toString() : "");
                    }
                }

                // Auto-size nas colunas
                for (int i = 0; i < tableView.getColumns().size(); i++) {
                    sheet.autoSizeColumn(i);
                }

                // Salvar arquivo
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    workbook.write(fos);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
