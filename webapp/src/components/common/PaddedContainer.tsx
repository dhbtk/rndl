import * as React from 'react';
import { ReactNode } from 'react';

export interface Props {
    narrow?: boolean;
    height?: number;
}

export default function PaddedContainer({ children, narrow, height }: Props & { children?: ReactNode }) {
    const actualHeight = height || 1;
    return (
        <div
            style={{
                margin: '0 auto',
                maxWidth: narrow ? '900px' : '100%',
                paddingLeft: '24px',
                paddingRight: '24px',
                paddingTop: `${16 + actualHeight * 64}px`
            }}
        >
            {children}
        </div>
    );
}