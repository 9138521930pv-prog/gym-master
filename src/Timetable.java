import java.util.*;

public class Timetable {
    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> schedule =
            new HashMap<>();

    public void addNewTrainingSession(TrainingSession session) {
        DayOfWeek day = session.getDayOfWeek();
        TimeOfDay time = session.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = schedule.get(day);
        if (daySchedule == null) {
            daySchedule = new TreeMap<>();
            schedule.put(day, daySchedule);
        }

        List<TrainingSession> sessionsAtTime = daySchedule.get(time);
        if (sessionsAtTime == null) {
            sessionsAtTime = new ArrayList<>();
            daySchedule.put(time, sessionsAtTime);
        }

        sessionsAtTime.add(session);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek day) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = schedule.get(day);
        if (daySchedule == null) {
            return Collections.emptyList();
        }

        List<TrainingSession> allSessions = new ArrayList<>();
        for (List<TrainingSession> sessionList : daySchedule.values()) {
            allSessions.addAll(sessionList);
        }
        return allSessions;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek day, TimeOfDay time) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = schedule.get(day);
        if (daySchedule == null) {
            return Collections.emptyList();
        }

        List<TrainingSession> sessions = daySchedule.get(time);
        return (sessions != null) ? sessions : Collections.emptyList();
    }
}