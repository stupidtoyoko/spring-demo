package com.example.demo.service;

import com.example.demo.exception.NoAvailableSeatsException;
import com.example.demo.model.Maintenance;
import com.example.demo.model.Schedule;
import com.example.demo.model.Ticket;
import com.example.demo.model.Visitor;
import com.example.demo.repository.MaintenanceRepository;
import com.example.demo.repository.ScheduleRepository;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.VisitorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepo;
    private final ScheduleRepository scheduleRepo;
    private final VisitorRepository visitorRepo;
    private final MaintenanceRepository maintenanceRepo;

    public TicketService(TicketRepository ticketRepo,
                         ScheduleRepository scheduleRepo,
                         VisitorRepository visitorRepo,
                         MaintenanceRepository maintenanceRepo) {
        this.ticketRepo = ticketRepo;
        this.scheduleRepo = scheduleRepo;
        this.visitorRepo = visitorRepo;
        this.maintenanceRepo = maintenanceRepo;
    }

    public Ticket buyTicket(Ticket ticket) {

        Schedule schedule = scheduleRepo.findById(ticket.getSchedule().getId())
                .orElse(null);

        if (schedule == null) {
            throw new RuntimeException("Расписание не найдено");
        }

        Visitor visitor = visitorRepo.findById(ticket.getVisitor().getId())
                .orElse(null);

        if (visitor == null) {
            throw new RuntimeException("Посетитель не найден");
        }

        ticket.setSchedule(schedule);
        ticket.setVisitor(visitor);

        long sold = ticketRepo.countBySchedule(schedule);
        int capacity = schedule.getAttraction().getCapacity();

        if (sold >= capacity) {
            throw new NoAvailableSeatsException("Нет свободных мест на это время");
        }

        List<Maintenance> maintenances =
                maintenanceRepo.findByAttraction(schedule.getAttraction());

        if (maintenances != null) {
            for (Maintenance m : maintenances) {

                if (m.getStartTime() == null || m.getEndTime() == null) continue;

                boolean overlaps =
                        schedule.getStartTime().isBefore(m.getEndTime()) &&
                                schedule.getEndTime().isAfter(m.getStartTime());

                if (overlaps) {
                    throw new RuntimeException("Аттракцион находится на обслуживании");
                }
            }
        }

        return ticketRepo.save(ticket);
    }

    public boolean isScheduleAvailable(Schedule schedule) {

        Schedule dbSchedule = scheduleRepo.findById(schedule.getId())
                .orElse(null);

        if (dbSchedule == null) return false;

        long sold = ticketRepo.countBySchedule(dbSchedule);
        int capacity = dbSchedule.getAttraction().getCapacity();

        if (sold >= capacity) return false;

        List<Maintenance> maintenances =
                maintenanceRepo.findByAttraction(dbSchedule.getAttraction());

        if (maintenances != null) {
            for (Maintenance m : maintenances) {

                if (m.getStartTime() == null || m.getEndTime() == null) continue;

                boolean overlaps =
                        dbSchedule.getStartTime().isBefore(m.getEndTime()) &&
                                dbSchedule.getEndTime().isAfter(m.getStartTime());

                if (overlaps) return false;
            }
        }

        return true;
    }
}