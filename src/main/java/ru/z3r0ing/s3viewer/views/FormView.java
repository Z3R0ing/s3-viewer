package ru.z3r0ing.s3viewer.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.z3r0ing.s3viewer.dto.S3Credentials;
import ru.z3r0ing.s3viewer.service.S3Service;

@PageTitle("Check S3 Connection")
@Route
public class FormView extends VerticalLayout {

    private final S3Service s3Service;

    private final TextField accessKeyField;
    private final PasswordField secretAccessKeyField;
    private final TextField regionNameField;
    private final TextField endpointUrlField;
    /*private final TextField bucketField;*/
    private final Checkbox pathStyleAccessCheckbox;
    private final Button checkConnectionBtn;

    public FormView(S3Service s3Service) {

        this.s3Service = s3Service;

        this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        this.setAlignItems(Alignment.CENTER);

        FormLayout formLayout = new FormLayout();

        accessKeyField = new TextField();
        accessKeyField.setLabel("Access key");
        accessKeyField.setPlaceholder("Access key");
        accessKeyField.setRequired(true);
        formLayout.add(accessKeyField);

        secretAccessKeyField = new PasswordField();
        secretAccessKeyField.setLabel("Secret access key");
        secretAccessKeyField.setRequired(true);
        formLayout.add(secretAccessKeyField);

        regionNameField = new TextField();
        regionNameField.setLabel("Region name");
        regionNameField.setPlaceholder("Region name");
        regionNameField.setRequired(true);
        formLayout.add(regionNameField);

        endpointUrlField = new TextField();
        endpointUrlField.setLabel("Endpoint URL");
        endpointUrlField.setPlaceholder("Endpoint URL");
        formLayout.add(endpointUrlField);

        /*bucketField = new TextField();
        bucketField.setLabel("Bucket");
        bucketField.setPlaceholder("Bucket");
        bucketField.setRequired(true);
        formLayout.add(bucketField);*/

        pathStyleAccessCheckbox = new Checkbox();
        pathStyleAccessCheckbox.setLabel("Path style access");
        formLayout.add(pathStyleAccessCheckbox);

        checkConnectionBtn = new Button("Check connection");
        checkConnectionBtn.addClickListener(e -> {
            Notification.show(s3Service.setS3Credentials(getS3Credentials()) ? "GOOD" : "BAD",
                    5000, Notification.Position.MIDDLE);
        });
        checkConnectionBtn.addClickShortcut(Key.ENTER);
        formLayout.add(checkConnectionBtn);

        formLayout.setWidth("600px");
        setAlignSelf(Alignment.CENTER, formLayout);
        add(formLayout);
    }

    private S3Credentials getS3Credentials() {
        return new S3Credentials(accessKeyField.getValue(),
                secretAccessKeyField.getValue(),
                regionNameField.getValue(),
                endpointUrlField.getValue(),
                //bucketField.getValue(),
                "",
                pathStyleAccessCheckbox.getValue());
    }
}
