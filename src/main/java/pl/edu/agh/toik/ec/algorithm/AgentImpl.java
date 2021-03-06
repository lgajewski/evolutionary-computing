package pl.edu.agh.toik.ec.algorithm;

import org.apfloat.Apcomplex;
import org.apfloat.ApcomplexMath;
import org.apfloat.Apfloat;
import pl.edu.agh.toik.ec.algorithm.diversity.DiversityCalculator;
import pl.edu.agh.toik.ec.algorithm.generation.PopulationGenerationStrategy;
import pl.edu.agh.toik.ec.communication.Message;
import pl.edu.agh.toik.ec.workers.SimpleMessage;
import pl.edu.agh.toik.ec.workers.Worker;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.ToDoubleFunction;
import java.util.stream.DoubleStream;

public class AgentImpl implements Agent {

    List<Individual> population;
    private LinkedList<AlgorithmStep> algorithmSteps = new LinkedList<>();

    private LinkedList<Message> incomingMessages = new LinkedList<>();
    private LinkedList<SimpleMessage> outgoingMessages = new LinkedList<>();

    private Property<Double> bestFitnessProperty = new Property<>();
    private Property<Double> worstFitnessProperty = new Property<>();
    private Property<Double> populationDiversityProperty = new Property<>();

    private Worker worker;
    private String name;

    private List<String> neighbours;

    private int populationSize;
    private int populationDimension;

    private PopulationGenerationStrategy populationGenerationStrategy;

    private boolean initialized = false;

    private final static ToDoubleFunction<Individual> INDIVIDUAL_TO_FITNESS_FUNCTION = new ToDoubleFunction<Individual>() {

        @Override
        public double applyAsDouble(Individual value) {
            return value.getFitness();
        }
    };

    @Override
    public void setup() {
        assert populationGenerationStrategy != null : "Population generation strategy cannot be null";
        population = populationGenerationStrategy.generatePopulation(this);
        assert population != null : "Setup population cannot be null";
        initialized = true;
    }

    @Override
    public void makeStep() {
        assert initialized : "Agent has to be initialized first";
        for (AlgorithmStep step : algorithmSteps)
            step.process(this, population);
        //calculatePopulationDiversity();
        calculateBestFitnessProperty();
		calculateWorstFitnessProperty();
        flushOutgoingMessages();
    }

    private void flushOutgoingMessages() {
        for (SimpleMessage message : outgoingMessages) {
            worker.sendMessage(message);
        }
        outgoingMessages.clear();
    }

    private void calculateBestFitnessProperty() {
        DoubleStream populationFitnessStream = population.stream().mapToDouble(INDIVIDUAL_TO_FITNESS_FUNCTION);
        bestFitnessProperty.setValue(populationFitnessStream.max().orElseGet(null));
    }

	 private void calculateWorstFitnessProperty() {
        DoubleStream populationFitnessStream = population.stream().mapToDouble(INDIVIDUAL_TO_FITNESS_FUNCTION);
        worstFitnessProperty.setValue(populationFitnessStream.min().orElseGet(null));
    }
	
    double calculatePopulationDiversity() {
        DiversityCalculator diversityCalculator = new DiversityCalculator(population, populationSize, populationDimension);
        return diversityCalculator.calculatePopulationDiversity();
    }

    @Override
    public void clearSteps() {
        algorithmSteps.clear();
    }

    @Override
    public boolean removeStep(AlgorithmStep step) {
        assert step != null : "Algorithm step cannot be null";
        return algorithmSteps.removeAll(Collections.singleton(step));
    }

    @Override
    public void addStep(AlgorithmStep step) {
        assert step != null : "Algorithm step cannot be null";
        algorithmSteps.add(step);
    }

    @Override
    public void addStep(int index, AlgorithmStep step) {
        assert step != null : "Algorithm step cannot be null";
        algorithmSteps.add(index, step);
    }

    @Override
    public void receiveMessage(Message message) {
        incomingMessages.add(message);
    }

    @Override
    public void sendMessage(SimpleMessage message) {
        outgoingMessages.add(message);
    }

    @Override
    public List<Message> getIncomingMessages() {
        return new ArrayList<>(incomingMessages);
    }

    @Override
    public boolean consumeIncomingMessage(Message message) {
        return incomingMessages.remove(message);
    }

    @Override
    public void setBestFitnessProperty(Property<Double> bestFitnessProperty) {
        this.bestFitnessProperty = bestFitnessProperty;
    }

    @Override
    public void setWorstFitnessProperty(Property<Double> worstFitnessProperty) {
        this.worstFitnessProperty = worstFitnessProperty;
    }

    @Override
    public void setPopulationDiversityProperty(Property<Double> populationDiversityProperty) {
        this.populationDiversityProperty = populationDiversityProperty;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<String> getNeighbours() {
        return neighbours;
    }

    @Override
    public void setNeighbours(List<String> neighbours) {
        this.neighbours = neighbours;
    }

    @Override
    public int getPopulationSize() {
        return populationSize;
    }

    @Override
    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    @Override
    public int getPopulationDimension() {
        return populationDimension;
    }

    @Override
    public void setPopulationDimension(int populationDimension) {
        this.populationDimension = populationDimension;
    }

    @Override
    public PopulationGenerationStrategy getPopulationGenerationStrategy() {
        return populationGenerationStrategy;
    }

    @Override
    public void setPopulationGenerationStrategy(PopulationGenerationStrategy populationGenerationStrategy) {
        this.populationGenerationStrategy = populationGenerationStrategy;
    }

    @Override
    public void setWorker(Worker worker) {
        this.worker = worker;
    }

}
