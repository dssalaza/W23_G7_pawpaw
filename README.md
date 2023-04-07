# PawPaw app
An Android mobile booking application that connects busy pet owners with mobile pet grooming professionals. 

## Getting Started

PawPaw integrates with third parties such as [firebase](https://firebase.google.com/) and [Stripe](https://stripe.com/en-ca). In order to run the project properly you'll need to follow the next steps:

### Prerequisites

PawPaw communicates with firebase, therefore it needs a `google-services.json` file. For security reasons this file is not versioned so please ask for this file to the contributors of the project and place it in the `app/` directory of the project.
```
app/google-services.json
```

In order to use google maps to get the current location of the user, we need a MAPS_API_KEY. Please ask for this to the contributors of the project and put the value in local.properties file in your project.

```
MAPS_API_KEY = "YOUR_API_KEY"
```

In order to make payments the application needs to communicate with Stripe. In order to do so it needs a `STRIPE_SECRET_KEY` and a `STRIPE_PUBLISHABLE_KEY`. Please ask for this file to the contributors of the project and replace it in the `CheckOutActivity.java`

```
On CheckOutActivity.java add the respective values to each secret key
    
final String STRIPE_SECRET_KEY = "<STRIPE_SECRET_KEY>";
final String STRIPE_PUBLISHABLE_KEY = "<STRIPE_PUBLISHABLE_KEY>";
```
