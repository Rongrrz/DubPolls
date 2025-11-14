import { useAtom } from 'jotai';
import { StyledCard } from '../StyledCard';
import { newsPostAtom } from '../../stores/NewsPosts';

// TODO: Add time ago was the new publish
export function LocalNews() {
  const [posts] = useAtom(newsPostAtom);
  return (
    <StyledCard title="Local News" className="h-full">
      <ul className="space-y-2">
        {posts.map(post => {
          return (
            <li key={post.title}>
              <a className="font-medium hover:underline" href={post.url} target="_blank" rel="noreferrer">
                {post.title}
              </a>
              <div className="text-xs text-neutral-500">{post.source}</div>
            </li>
          );
        })}
      </ul>
    </StyledCard>
  );
}
