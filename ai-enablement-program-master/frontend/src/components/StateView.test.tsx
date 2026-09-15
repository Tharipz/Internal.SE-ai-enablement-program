import { render, screen } from '@testing-library/react';

import { StateView } from './StateView';

test('renders frontend smoke state', () => {
  render(<StateView title="Backend unavailable" description="Start Quarkus" />);

  expect(screen.getByText('Start Quarkus')).toBeInTheDocument();
});
