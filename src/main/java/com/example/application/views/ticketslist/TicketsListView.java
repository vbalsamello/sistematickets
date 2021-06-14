package com.example.application.views.ticketslist;

import java.util.Optional;

import com.example.application.data.entity.Ticket;
import com.example.application.data.service.TicketService;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.example.application.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextField;

@Route(value = "Tickets/:ticketID?/:action?(edit)", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("TicketsList")
public class TicketsListView extends Div implements BeforeEnterObserver {

    private final String TICKET_ID = "ticketID";
    private final String TICKET_EDIT_ROUTE_TEMPLATE = "Tickets/%d/edit";

    private Grid<Ticket> grid = new Grid<>(Ticket.class, false);

    private TextField descripcion;
    private TextField estado;
    private TextField categoria;
    private TextField prioridad;
    private DatePicker fechaCreacion;
    private DatePicker fechaUltimoEstado;
    private TextField mailCOntacto;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Ticket> binder;

    private Ticket ticket;

    private TicketService ticketService;

    public TicketsListView(@Autowired TicketService ticketService) {
        this.ticketService = ticketService;
        addClassNames("tickets-list-view", "flex", "flex-col", "h-full");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("descripcion").setAutoWidth(true);
        grid.addColumn("estado").setAutoWidth(true);
        grid.addColumn("categoria").setAutoWidth(true);
        grid.addColumn("prioridad").setAutoWidth(true);
        grid.addColumn("fechaCreacion").setAutoWidth(true);
        grid.addColumn("fechaUltimoEstado").setAutoWidth(true);
        grid.addColumn("mailCOntacto").setAutoWidth(true);
        grid.setItems(query -> ticketService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(TICKET_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(TicketsListView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Ticket.class);

        // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.ticket == null) {
                    this.ticket = new Ticket();
                }
                binder.writeBean(this.ticket);

                ticketService.update(this.ticket);
                clearForm();
                refreshGrid();
                Notification.show("Ticket details stored.");
                UI.getCurrent().navigate(TicketsListView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the ticket details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> ticketId = event.getRouteParameters().getInteger(TICKET_ID);
        if (ticketId.isPresent()) {
            Optional<Ticket> ticketFromBackend = ticketService.get(ticketId.get());
            if (ticketFromBackend.isPresent()) {
                populateForm(ticketFromBackend.get());
            } else {
                Notification.show(String.format("The requested ticket was not found, ID = %d", ticketId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(TicketsListView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("flex flex-col");
        editorLayoutDiv.setWidth("400px");

        Div editorDiv = new Div();
        editorDiv.setClassName("p-l flex-grow");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        descripcion = new TextField("Descripcion");
        estado = new TextField("Estado");
        categoria = new TextField("Categoria");
        prioridad = new TextField("Prioridad");
        fechaCreacion = new DatePicker("Fecha Creacion");
        fechaUltimoEstado = new DatePicker("Fecha Ultimo Estado");
        mailCOntacto = new TextField("Mail C Ontacto");
        Component[] fields = new Component[]{descripcion, estado, categoria, prioridad, fechaCreacion,
                fechaUltimoEstado, mailCOntacto};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getLazyDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Ticket value) {
        this.ticket = value;
        binder.readBean(this.ticket);

    }
}
