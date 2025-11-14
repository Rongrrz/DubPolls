import { useAtom } from 'jotai';
import { transportAtom } from '../../stores/Transportation';
import { StyledCard } from '../StyledCard';

// TODO: TIME AGO
export function Transportation() {
  const [posts] = useAtom(transportAtom);

  return (
    <StyledCard title="Transportation Updates" className="h-full">
      <ul className="space-y-2">
        {posts.map(post => {
          return (
            <li key={post.title} className="flex items-start justify-between">
              <div>
                <div className="font-medium">{post.title}</div>
                <div className="text-xs text-neutral-500">
                  {post.details}· {post.source}
                </div>
              </div>
              <span className="rounded-full bg-yellow-500/20 px-2 py-0.5 text-xs text-yellow-300">
                {post.badge}
              </span>
            </li>
          );
        })}
      </ul>
    </StyledCard>
  );
}
