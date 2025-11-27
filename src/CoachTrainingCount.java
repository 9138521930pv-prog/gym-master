class CoachTrainingCount implements Comparable<CoachTrainingCount> {
    private final Coach coach;
    private int count;

    public CoachTrainingCount(Coach coach, int count) {
        this.coach = coach;
        this.count = count;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getCount() {
        return count;
    }

    public void increment() {
        this.count++;
    }

    @Override
    public int compareTo(CoachTrainingCount other) {
        // Сортируем по убыванию количества тренировок
        return Integer.compare(other.count, this.count);
    }
}