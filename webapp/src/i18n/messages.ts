export interface Messages {
    [key: string]: { [key: string]: string };
}

const messages: Messages = {
    en: {
        // LoginRoute
        'login.invalidCredentials': 'Your email address or password are incorrect.',
        'login.emailAddressLabel': 'Email address',
        'login.passwordLabel': 'Password',
        'login.signInLabel': 'Sign in',
        // NavigationDrawer
        'profileMenu.profile': 'Profile',
        'profileMenu.signOut': 'Sign out',
        'drawerMenu.dashboard': 'Dashboard',
        'drawerMenu.vehicles': 'Vehicles',
        'drawerMenu.trips': 'Trips',
        // TripListRoute
        'tripList.title': 'Trips',
        'tripList.distanceTraveledThisMonth': 'Distance traveled this month',
        'tripList.distanceAxisLabel': 'Distance',
        'tripList.distanceLabel': '{km} km',
        'tripList.distanceInDay': 'traveled in {date}',
        'tripList.traveledThisMonth': 'traveled this month',
        'tripList.tripsOnDay': '{count, plural, one {#trip} other {# trips}}',
        'tripList.economyLabel': '{economy} km/l',
        'tripList.speedLabel': '{speed} km/h',
        'tripList.timeHeader': 'Time',
        'tripList.durationHeader': 'Duration',
        'tripList.vehicleHeader': 'Vehicle',
        'tripList.distanceHeader': 'Distance',
        'tripList.economyHeader': 'Economy',
        'tripList.averageSpeedHeader': 'Average speed'
    }
};

export default messages;