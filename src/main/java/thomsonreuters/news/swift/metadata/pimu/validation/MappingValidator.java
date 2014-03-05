package thomsonreuters.news.swift.metadata.pimu.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MappingValidator {
	private float validRatio;

	private Logger logger = LoggerFactory.getLogger(MappingValidator.class);

	public MappingValidator(float validRatio) {
		this.validRatio = validRatio;
	}

	public boolean isMappingValid(int inactiveTableRowsNum,
			int activeTableRowsNum) {
		if ((activeTableRowsNum == inactiveTableRowsNum)
				|| (inactiveTableRowsNum == 0)) {
			logger.info(String.format("Mapping is valid. Valid ratio is %s.",
					validRatio));
			return true;
		}
		float ratio1 = inactiveTableRowsNum / activeTableRowsNum;
		float ratio2 = activeTableRowsNum / inactiveTableRowsNum;
		if ((ratio1 <= validRatio) || (ratio2 <= validRatio)) {
			logger.info(String.format("Mapping is valid. Valid ratio is %s.",
					validRatio));
			return true;
		} else {
			logger.info(String.format(
					"Mapping is not valid. Valid ratio is %s.", validRatio));
			return false;
		}
	}
}
