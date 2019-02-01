package eu.javaexperience.data.build;

import eu.javaexperience.data.DataPhenomenon;
import eu.javaexperience.data.DataPhenomenonClass;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.patterns.creational.builder.unit.InstanceBuilder;
import eu.javaexperience.verify.EnvironmentDependValidator;

public class DataPhenomenonBuilder extends InstanceBuilder<DataPhenomenonFields, DataPhenomenon>
{
	public static final GetBy1<DataPhenomenon, InstanceBuilder<DataPhenomenonFields, DataPhenomenon>> CREATOR = new GetBy1<DataPhenomenon, InstanceBuilder<DataPhenomenonFields, DataPhenomenon>>()
	{
		@Override
		public DataPhenomenon getBy
		(
			InstanceBuilder<DataPhenomenonFields, DataPhenomenon> a
		)
		{
			return new BuiltDataPhenomenon
			(
				(Integer) a.get(DataPhenomenonFields.fieldDataLength),
				(DataPhenomenonClass) a.get(DataPhenomenonFields.dataClass),
				(EnvironmentDependValidator)a.get(DataPhenomenonFields.validator)
			);
		}
	};
	
	public DataPhenomenonBuilder()
	{
		super(DataPhenomenonFields.class, CREATOR);
	}
}
