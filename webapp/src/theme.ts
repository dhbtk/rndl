import createMuiTheme from 'material-ui/styles/createMuiTheme';

const theme = createMuiTheme({
    palette: {
        primary: {
            main: '#ffb74d',
            light: '#ffe97d',
            dark: '#c88719',
            contrastText: '#000000'
        },
        secondary: {
            main: '#4fc3f7',
            light: '#8bf6ff',
            dark: '#0093c4',
            contrastText: '#000000'
        }
    }
});

export default theme;