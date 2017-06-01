package pl.edu.agh.toik.ec.configuration;

import pl.edu.agh.toik.ec.algorithm.generation.PopulationGenerationStrategy;

import java.util.Map;

/**
 * Created by M on 2017-05-30.
 */
public class AgentConfiguration {

    private int populationSize;
    private int populationDimension;
    private PopulationGenerationStrategy populationGenerationStrategy;

    Map<AgentParameter, PropertyConfiguration> getParameterConfiguration(){};

}