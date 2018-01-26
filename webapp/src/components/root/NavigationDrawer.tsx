import User from '../../api/entities/user/User';
import { MenuItem, WithStyles } from 'material-ui';
import { StyleRules, Theme } from 'material-ui/styles';
import * as React from 'react';
import { SyntheticEvent } from 'react';
import withStyles from 'material-ui/styles/withStyles';
import Typography from 'material-ui/Typography';
import Divider from 'material-ui/Divider';
import Toolbar from 'material-ui/Toolbar';
import Drawer from 'material-ui/Drawer';
import IconButton from 'material-ui/IconButton';
import Icon from 'material-ui/Icon';
import Menu from 'material-ui/Menu';
import { logOut } from '../../api/services/LoginService';

export interface Props {
    user: User;
    open: boolean;
}

export interface State {
    menuAnchor?: HTMLElement;
}

type StyleProps = Props & WithStyles<string>;

const styles = (theme: Theme): StyleRules => ({
    header: theme.mixins.toolbar,
    drawerPaper: {
        width: 300
    },
    flex: {
        flex: 1
    }
});

class NavigationDrawer extends React.Component<StyleProps, State> {
    constructor(props: StyleProps) {
        super(props);
        this.state = {
            menuAnchor: undefined
        };

        this.openMenu = this.openMenu.bind(this);
        this.closeMenu = this.closeMenu.bind(this);
        this.logOut = this.logOut.bind(this);
    }

    openMenu(event: SyntheticEvent<HTMLElement>) {
        this.setState({ menuAnchor: event.currentTarget });
    }

    closeMenu() {
        this.setState({ menuAnchor: undefined });
    }

    logOut() {
        logOut();
    }

    render() {
        const { user, open, classes } = this.props;
        const menuOpen = this.state.menuAnchor !== undefined;

        return (
            <Drawer type="permanent" open={open} classes={{ paper: classes.drawerPaper }}>
                <div className={classes.header}>
                    <Toolbar>
                        <Typography className={classes.flex}>{user.email}</Typography>
                        <IconButton onClick={this.openMenu}>
                            <Icon>account_circle</Icon>
                        </IconButton>
                        <Menu
                            anchorEl={this.state.menuAnchor}
                            anchorOrigin={{
                                horizontal: 'left',
                                vertical: 'top'
                            }}
                            open={menuOpen}
                            onClose={this.closeMenu}
                        >
                            <MenuItem onClick={this.closeMenu}>Profile</MenuItem>
                            <MenuItem onClick={this.logOut}>Log out</MenuItem>
                        </Menu>
                    </Toolbar>
                </div>
                <Divider/>
            </Drawer>
        );
    }
}

export default withStyles(styles)(NavigationDrawer) as React.ComponentClass<Props>;