import { atom, getDefaultStore } from 'jotai';
import { summaryAtom } from './Summary';
import { redditPostsAtom } from './RedditPosts';
import { weatherAtom } from './Weather';
import { newsPostAtom } from './NewsPosts';
import { transportAtom } from './Transportation';

const lastFetchedAtom = atom<number>(0);

const store = getDefaultStore();

const FIVE_MINUTES = 5 * 60 * 1000;

/**
 * Fetches the server for data, and supplies atoms with updated data.
 * NOTE: Will only fetch the server if not fetched within the last 300 seconds.
 */
export async function FetchServer(): Promise<void> {
  const lastFetched = store.get(lastFetchedAtom);

  // We'll skip fetching if the time difference is within absolute 5 minutes.
  if (Math.abs(Date.now() - lastFetched) < FIVE_MINUTES) {
    console.log('[FetchServer] Fetching too frequently!');
    return;
  }

  try {
    console.log('[FetchServer] Fetching data…');

    const res = await fetch('http://localhost:8080/api/fetch');
    const data = await res.json();
    // console.log(data);

    // Update all our atoms...
    store.set(redditPostsAtom, data.redditPosts);
    // store.set(weatherAtom, data.weather);
    // store.set(newsPostAtom, data.news);
    // store.set(transportAtom, data.transport);
    // store.set(summaryAtom, data.summary);

    // Save time to lastFetched if successful fetch.
    store.set(lastFetchedAtom, Date.now());
    console.log('[FetchServer] Updated atoms with new server data.');
  } catch (err) {
    console.error('[FetchServer] Failed:', err);
  }
}
