/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import React, {useState} from 'react';
import {
	Bar,
	BarChart,
	Cell,
	LabelList,
	ResponsiveContainer,
	Tooltip,
	XAxis,
	YAxis,
} from 'recharts';

import TooltipContent from '../TooltipContent.es';

export default ({data, height, totalEntries, width}) => {
	const [activeIndex, setActiveIndex] = useState(null);

	const handleOnMouseOut = () => {
		setActiveIndex(null);
	};

	const handleOnMouseOver = (index) => {
		setActiveIndex(index);
	};

	return (
		<ResponsiveContainer height={height || '99%'} width={width || '99%'}>
			<BarChart
				data={data}
				layout="vertical"
				margin={{
					bottom: 20,
					left: 20,
					right: 20,
					top: 20,
				}}
			>
				<XAxis
					axisLine={{stroke: '#CDCED9'}}
					tickLine={false}
					type="number"
				/>

				<YAxis
					dataKey="label"
					stroke="#272833"
					tickLine={false}
					tickMargin={16}
					type="category"
					width={214}
				/>

				<Tooltip
					content={
						<TooltipContent
							activeIndex={activeIndex}
							totalEntries={totalEntries}
						/>
					}
					cursor={{fill: 'transparent'}}
				/>

				<Bar
					barCategoryGap={30}
					barGap={5}
					dataKey="count"
					fill="#4B9BFF"
					onMouseOut={handleOnMouseOut}
					onMouseOver={(_, index) => handleOnMouseOver(index)}
				>
					{data.map((_, index) => (
						<Cell
							fill={
								activeIndex == null || activeIndex === index
									? 'rgba(75, 155, 255)'
									: 'rgba(75, 155, 255, 0.5)'
							}
							key={`cell-${index}`}
						/>
					))}

					<LabelList
						dataKey="count"
						fill="rgba(255, 255, 255)"
						offset={16}
						position="insideRight"
					/>
				</Bar>
			</BarChart>
		</ResponsiveContainer>
	);
};
