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

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = schedule.get(dayOfWeek);
        if (daySchedule != null) {
            return daySchedule;
        }
        return new TreeMap<>();
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek day, TimeOfDay time) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = schedule.get(day);
        if (daySchedule == null) {
            return Collections.emptyList();
        }

        List<TrainingSession> sessions = daySchedule.get(time);
        return (sessions != null) ? sessions : Collections.emptyList();
    }

    public List<CoachTrainingCount> getCountByCoaches() {
        Map<Coach, CoachTrainingCount> coachMap = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> dailySchedule : schedule.values()) {
            for (List<TrainingSession> sessionList : dailySchedule.values()) {
                for (TrainingSession session : sessionList) {
                    Coach coach = session.getCoach();
                    if (coach == null) {
                        continue;
                    }

                    CoachTrainingCount counter = coachMap.get(coach);
                    if (counter == null) {
                        counter = new CoachTrainingCount(coach, 1);
                        coachMap.put(coach, counter);
                    } else {
                        counter.increment();
                    }
                }
            }
        }

        List<CoachTrainingCount> resultList = new ArrayList<>();
        for (CoachTrainingCount counter : coachMap.values()) {
            resultList.add(counter);
        }

        Collections.sort(resultList);

        return resultList;
    }
}