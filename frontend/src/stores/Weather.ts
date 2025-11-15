import { atom } from 'jotai';
import type { WeatherSummary } from '../types/Homepage';

export const weatherAtom = atom<WeatherSummary>({
  temperature: 60,
  windMPH: 10,
  rainChance: 80,
});
