package scheduler.view;

import scheduler.controller.AppointmentService;
import scheduler.model.Appointment;

import javax.swing.AbstractListModel;
import java.util.List;

public class AppointmentListModel extends AbstractListModel<String> {

    private static final long serialVersionUID = 1L;
    private List<Appointment> appointments;

    public AppointmentListModel() {
        refresh(); // Load from DB on construction
    }

    public void refresh() {
        appointments = new AppointmentService().getAppointments();
        fireContentsChanged(this, 0, getSize());
    }

    @Override
    public int getSize() {
        return appointments == null ? 0 : appointments.size();
    }

    @Override
    public String getElementAt(int index) {
        Appointment apptointment = appointments.get(index);
        return apptointment.getDate() + " — " + apptointment.getTitle(); // Customize format
    }

    public Appointment getAppointmentAt(int index) {
        return appointments.get(index);
    }
}
