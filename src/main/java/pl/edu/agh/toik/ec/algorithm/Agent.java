package pl.edu.agh.toik.ec.algorithm;

import java.util.List;

import pl.edu.agh.toik.ec.algorithm.generation.PopulationGenerationStrategy;
import pl.edu.agh.toik.ec.communication.Message;

public interface Agent {

    public void setup();

    public void makeStep();

    public void clearSteps();

    public boolean removeStep(AlgorithmStep step);

    public void addStep(AlgorithmStep step);

    public void addStep(int index, AlgorithmStep step);

    public void receiveMessage(Message message);

    public void sendMessage(String address, Message message);

    public List<Message> getOutgoingMessages();

    public boolean consumeOutgoingMessage(Message message);

    public List<Message> getIncomingMessages();

    public boolean consumeIncomingMessage(Message message);

    public void setBestFitnessProperty(Property<Double> bestFitnessProperty);

    public void setWorstFitnessProperty(Property<Double> worstFitnessProperty);

    public void setPopulationDiversityProperty(Property<Double> populationDiversityProperty);

    public String getName();

    public void setName(String name);

    public List<Agent> getNeighbours();

    public void setNeighbours(List<Agent> neighbours);

    public int getPopulationSize();

    public void setPopulationSize(int populationSize);

    public int getPopulationDimension();

    public void setPopulationDimension(int populationDimension);

    public PopulationGenerationStrategy getPopulationGenerationStrategy();

    public void setPopulationGenerationStrategy(PopulationGenerationStrategy populationGenerationStrategy);

}
