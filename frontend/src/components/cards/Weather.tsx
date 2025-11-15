import { useAtom } from 'jotai';
import { StyledCard } from '../StyledCard';
import { weatherAtom } from '../../stores/Weather';

export function Weather() {
  const [weather] = useAtom(weatherAtom);

  return (
    <StyledCard title="Current Weather" className="h-full">
      <div className="flex items-end gap-4">
        <div>
          <div className="text-4xl font-black">{weather.temperature}°F</div>
          <div className="text-neutral-400">{weather.windMPH}mph</div>
        </div>
      </div>
      <hr className="my-3 border-neutral-800" />
      <ul className="text-sm leading-6 text-neutral-300">
        <li>{weather.rainChance}% to rain</li>
      </ul>
    </StyledCard>
  );
}
