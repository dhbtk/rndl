import { StyleRules, Theme } from 'material-ui/styles';
import { AppBarProps, default as AppBar } from 'material-ui/AppBar';
import { WithStyles } from 'material-ui';
import * as React from 'react';
import withStyles from 'material-ui/styles/withStyles';

const style = (theme: Theme): StyleRules => ({
    root: {
        width: 'calc(100% - 300px)'
    }
});

function PaddedAppBar(props: AppBarProps & WithStyles<string>) {
    const usedProps = {
        ...props,
        classes: {
            root: props.classes.root
        }
    };
    return <AppBar {...usedProps}>{props.children}</AppBar>;
}

export default withStyles(style)(PaddedAppBar) as React.StatelessComponent<AppBarProps>;