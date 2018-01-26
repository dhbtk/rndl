import * as React from 'react';
import { ReactNode } from 'react';

export interface Props {
    narrow?: boolean;
}

export default function PaddedContainer({ children, narrow }: Props & { children?: ReactNode }) {
    return (
        <div
            style={{
                margin: '0 auto',
                maxWidth: narrow ? '900px' : '100%',
                paddingLeft: '24px',
                paddingRight: '24px',
                paddingTop: '80px'
            }}
        >
            {children}
        </div>
    );
}