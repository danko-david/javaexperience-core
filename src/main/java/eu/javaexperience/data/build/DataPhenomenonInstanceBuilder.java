package eu.javaexperience.data.build;

import eu.javaexperience.data.DataPhenomenon;
import eu.javaexperience.data.DataPhenomenonEnvironment;
import eu.javaexperience.data.DataPhenomenonInstance;
import eu.javaexperience.data.DataPhenomenonUnit;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.patterns.creational.builder.unit.InstanceBuilder;
import eu.javaexperience.verify.EnvironmentDependValidator;

public class DataPhenomenonInstanceBuilder extends InstanceBuilder<DataPhenomenonInstanceFields, DataPhenomenonInstance>
{
	public static final GetBy1<DataPhenomenonInstance, InstanceBuilder<DataPhenomenonInstanceFields, DataPhenomenonInstance>> CREATOR = new GetBy1<DataPhenomenonInstance, InstanceBuilder<DataPhenomenonInstanceFields, DataPhenomenonInstance>>()
	{
		@Override
		public DataPhenomenonInstance getBy
		(
			InstanceBuilder<DataPhenomenonInstanceFields, DataPhenomenonInstance> a
		)
		{
			return new BuiltDataPhenomenonInstance
			(
				(DataPhenomenon) a.get(DataPhenomenonInstanceFields.phenomenon),
				(String) a.get(DataPhenomenonInstanceFields.name),
				(Boolean) a.get(DataPhenomenonInstanceFields.required),
				(EnvironmentDependValidator<DataPhenomenonEnvironment, DataPhenomenonUnit, String>) a.get(DataPhenomenonInstanceFields.validator)
			);
		}
	};
	
	public DataPhenomenonInstanceBuilder()
	{
		super(DataPhenomenonInstanceFields.class, CREATOR);
	}
}
