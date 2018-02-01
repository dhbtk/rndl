///<reference path="../../../node_modules/@types/react-intl/index.d.ts"/>
import User from '../../api/entities/user/User';
import { ListItem, ListItemIcon, ListItemText, MenuItem, WithStyles } from 'material-ui';
import { StyleRules, Theme } from 'material-ui/styles';
import * as React from 'react';
import { ReactNode, SyntheticEvent } from 'react';
import withStyles from 'material-ui/styles/withStyles';
import Typography from 'material-ui/Typography';
import Divider from 'material-ui/Divider';
import Toolbar from 'material-ui/Toolbar';
import Drawer from 'material-ui/Drawer';
import IconButton from 'material-ui/IconButton';
import Icon from 'material-ui/Icon';
import Menu from 'material-ui/Menu';
import { signOut } from '../../api/services/LoginService';
import List from 'material-ui/List';
import { Link } from 'react-router-dom';
import { FormattedMessage, InjectedIntlProps, injectIntl } from 'react-intl';

export interface Props {
    user: User;
    open: boolean;
}

export interface State {
    menuAnchor?: HTMLElement;
}

type InternalProps = Props & WithStyles<string> & InjectedIntlProps;

const styles = (theme: Theme): StyleRules => ({
    header: theme.mixins.toolbar,
    drawerPaper: {
        width: 300
    },
    flex: {
        flex: 1
    }
});

interface MenuLinkProps {
    to: string;
    children?: ReactNode;
}

function MenuLink({ to, children }: MenuLinkProps) {
    return (
        <ListItem button={true} component={props => <Link to={to} {...props}/>}>
            {children}
        </ListItem>
    );
}

class NavigationDrawer extends React.Component<InternalProps, State> {
    constructor(props: InternalProps) {
        super(props);
        this.state = {
            menuAnchor: undefined
        };

        this.openMenu = this.openMenu.bind(this);
        this.closeMenu = this.closeMenu.bind(this);
        this.signOut = this.signOut.bind(this);
    }

    openMenu(event: SyntheticEvent<HTMLElement>) {
        this.setState({ menuAnchor: event.currentTarget });
    }

    closeMenu() {
        this.setState({ menuAnchor: undefined });
    }

    signOut() {
        signOut();
    }

    render() {
        const { user, open, classes } = this.props;
        const menuOpen = this.state.menuAnchor !== undefined;
        const { formatMessage } = this.props.intl;

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
                            <MenuItem onClick={this.closeMenu}>
                                <FormattedMessage id="profileMenu.profile"/>
                            </MenuItem>
                            <MenuItem onClick={this.signOut}>
                                <FormattedMessage id="profileMenu.signOut"/>
                            </MenuItem>
                        </Menu>
                    </Toolbar>
                </div>
                <Divider/>
                <List>
                    <MenuLink to="/">
                        <ListItemIcon>
                            <Icon>dashboard</Icon>
                        </ListItemIcon>
                        <ListItemText primary={formatMessage({ id: 'drawerMenu.dashboard' })}/>
                    </MenuLink>
                    <MenuLink to="/vehicles">
                        <ListItemIcon>
                            <Icon>directions_car</Icon>
                        </ListItemIcon>
                        <ListItemText primary={formatMessage({ id: 'drawerMenu.vehicles' })}/>
                    </MenuLink>
                    <MenuLink to="/trips">
                        <ListItemIcon>
                            <Icon>traffic</Icon>
                        </ListItemIcon>
                        <ListItemText primary={formatMessage({ id: 'drawerMenu.trips' })}/>
                    </MenuLink>
                </List>
            </Drawer>
        );
    }
}

export default injectIntl<Props>(withStyles(styles)(NavigationDrawer));