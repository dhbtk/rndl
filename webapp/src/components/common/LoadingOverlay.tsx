import { StyleRules, Theme } from 'material-ui/styles';
import { CircularProgress, WithStyles } from 'material-ui';
import * as React from 'react';
import withStyles from 'material-ui/styles/withStyles';

const styles = (theme: Theme): StyleRules => ({
    overlay: {
        position: 'absolute',
        width: '100%',
        height: '100vh',
        background: 'rgba(255, 255, 255, 0.54)',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        top: 0,
        left: 0
    }
});

function LoadingOverlay({ classes }: WithStyles<'overlay'>) {
    return (
        <div className={classes.overlay}>
            <CircularProgress size={64}/>
        </div>
    );
}

export default withStyles(styles)(LoadingOverlay) as React.StatelessComponent<{}>;