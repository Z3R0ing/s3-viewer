package ru.z3r0ing.s3viewer.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.z3r0ing.s3viewer.dto.S3Item;
import ru.z3r0ing.s3viewer.service.S3Service;

@PageTitle("Browse S3")
@Route("/browser")
public class BrowserView extends VerticalLayout {

    private final S3Service s3Service;

    private final TextField folderField;
    private final Button backBtn;
    private final Grid<S3Item> grid;

    private String folder = "";

    public BrowserView(S3Service s3Service) {

        this.s3Service = s3Service;

        this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        this.setAlignItems(Alignment.CENTER);

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        folderField = new TextField();
        folderField.setReadOnly(true);
        folderField.setValue(folder);
        horizontalLayout.add(folderField);

        backBtn = new Button("Back");
        backBtn.addClickListener(event -> {
            if (!folder.equals("")) {
                String parentFolder = getParentFolder(folder);
                openFolder(parentFolder);
            }
        });
        backBtn.addClickShortcut(Key.BACKSPACE);
        horizontalLayout.add(backBtn);

        horizontalLayout.setWidth("600px");
        add(horizontalLayout);

        grid = new Grid<>(S3Item.class, false);

        grid.addComponentColumn(this::createFileTypeIcon).setSortable(false).setAutoWidth(true).setFlexGrow(0).setHeader("");
        grid.addColumn(S3Item::getPath).setComparator((s3Item1, s3Item2) ->
                s3Item1.getPath()
                        .compareToIgnoreCase(s3Item2.getPath())).setHeader("Name");

        grid.setItems(s3Service.getFolderItems(folder));

        grid.addItemDoubleClickListener(event -> {
            S3Item selectedS3Item = event.getItem();
            if (!selectedS3Item.isFile()) {
                openFolder(selectedS3Item.getPath());
            }
        });

        add(grid);
    }

    private Icon createFileTypeIcon(S3Item s3Item) {
        if (s3Item.isFile()) {
            return VaadinIcon.FILE.create();
        } else {
            return VaadinIcon.FOLDER.create();
        }
    }

    private void openFolder(String folder) {
        this.folder = folder;
        folderField.setValue(this.folder);
        grid.setItems(s3Service.getFolderItems(this.folder));
        grid.getDataProvider().refreshAll();
    }

    private String getParentFolder(String folder) {
        String[] folders = folder.split("/");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < folders.length - 1; i++) {
            sb.append(folders[i]).append("/");
        }
        return sb.toString();
    }

}
