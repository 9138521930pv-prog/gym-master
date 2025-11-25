import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

public class TimetableTest {
    private Timetable timetable;

    @BeforeEach
    void setUp() {
        timetable = new Timetable();
    }

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);
        TreeMap<TimeOfDay, List<TrainingSession>> mondaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        assertEquals(1, mondaySessions.size(),
                "За понедельник должно быть одно занятие");
        List<TrainingSession> sessions = mondaySessions.get(mondaySessions.firstKey());
        assertNotNull(sessions, "Занятие за понедельник должно присутствовать в списке");
        assertEquals(1, sessions.size(), "В 13:00 должно быть одно занятие");
        assertTrue(sessions.contains(singleTrainingSession),
                "Занятие за понедельник должно присутствовать в списке");
        assertEquals(singleTrainingSession, sessions.get(0),
                "Занятие в понедельник должно совпадать с добавленным");
        TreeMap<TimeOfDay, List<TrainingSession>> tuesdaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty(),
                "За вторник не должно быть временных слотов с занятиями");
        assertEquals(0, tuesdaySessions.size(),
                "Размер TreeMap за вторник должен быть равен 0");
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        TreeMap<TimeOfDay, List<TrainingSession>> mondaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size(),
                "За понедельник должно быть одно занятие");
        assertTrue(mondaySessions.values().stream()
                        .anyMatch(list -> list.contains(mondayChildTrainingSession)),
                "Занятие за понедельник должно присутствовать в списке");

        TreeMap<TimeOfDay, List<TrainingSession>> thursdaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursdaySessions.size(),
                "За четверг должно быть два занятия");

        List<TrainingSession> allThursdaySessions = thursdaySessions.values().stream()
                .flatMap(List::stream)                                                                                                                                                                                                                                                                                                                           .toList();

        TrainingSession firstSession = allThursdaySessions.get(0);
        TrainingSession secondSession = allThursdaySessions.get(1);

        assertEquals(new TimeOfDay(13, 0), firstSession.getTimeOfDay(),
                "Первое занятие в четверг должно начинаться в 13:00");
        assertEquals(new TimeOfDay(20, 0), secondSession.getTimeOfDay(),
                "Второе занятие в четверг должно начинаться в 20:00");

        assertSame(thursdayChildTrainingSession, firstSession,
                "Первое занятие в четверг должно быть детской акробатикой");
        assertSame(thursdayAdultTrainingSession, secondSession,
                "Второе занятие в четверг должно быть взрослой акробатикой");

        // Проверка вторника
        TreeMap<TimeOfDay, List<TrainingSession>> tuesdaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty(),
                "За вторник не должно быть ни одного занятия");
        assertEquals(0, tuesdaySessions.size(),
                "Размер списка занятий за вторник должен быть равен 0");
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> monday1300Sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        assertEquals(1, monday1300Sessions.size(),
                "За понедельник в 13:00 должно быть одно занятие");
        assertTrue(monday1300Sessions.contains(singleTrainingSession),
                "Занятие за понедельник в 13:00 должно присутствовать в списке");
        assertEquals(singleTrainingSession, monday1300Sessions.get(0),
                "Первое (и единственное) занятие в списке должно совпадать с добавленным");
        List<TrainingSession> monday1400Sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(monday1400Sessions.isEmpty(),
                "За понедельник в 14:00 не должно быть ни одного занятия");
        assertEquals(0, monday1400Sessions.size(),
                "Размер списка занятий за понедельник в 14:00 должен быть равен 0");
    }
}
