package com.zarea.instabugtask.domain.exception

class LocationPermissionNotGranted : Exception("Location permission not granted")
class LocationServiceNotEnabled : Exception("Location service not enabled")
class LocationProviderDisabled : Exception("Location provider disabled")
class NoLocationProvidersAvailable : Exception("No location providers available")
class NoInternetConnectionAndNoCachedDataAvailable :Exception("No internet connection and no cached data available")