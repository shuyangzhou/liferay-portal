<div align="center">
    <a href="https://travis-ci.org/liferay/com-liferay-vulcan">
        <img src="https://travis-ci.org/liferay/com-liferay-vulcan.svg?branch=master" alt="Travis CI" />
    </a>
    <a href='https://coveralls.io/github/liferay/com-liferay-vulcan?branch=master'>
        <img src='https://coveralls.io/repos/github/liferay/com-liferay-vulcan/badge.svg?branch=master' alt='Coverage Status' />
    </a>
    <a href="https://codebeat.co/projects/github-com-liferay-com-liferay-vulcan-master">
        <img alt="codebeat badge" src="https://codebeat.co/badges/bb871bcd-dd93-49f2-a8bc-6166169b0e44" />
    </a>
    <a href='https://www.gnu.org/licenses/lgpl-3.0'>
        <img src='https://img.shields.io/badge/License-LGPL%20v3-blue.svg' alt='License: LGPL v3' />
    </a>
</div>

# Vulcan Architect

Vulcan Architect is part of the [Vulcan project](#the-vulcan-project), which aims to promote the creation of APIs designed to evolve over time. The project also provides a set of guidelines for API providers and consumers that can be implemented in any technology, and Vulcan Consumer, a library to facilitate the creation of consumers of any hypermedia API.

Vulcan Architect is a server-side library that facilitates the creation of Vulcan REST APIs. It's also opinionated to reduce the amount of code API developers have to write. This is also achieved by out-of-the-box implementations of well known patterns in REST APIs, such as the Collection Pattern.

Two key techniques make this possible:

- **Hypermedia**: The same links and forms that we all use every day in a browser can also be applied to APIs to get the same great decoupling and flexibility. 

- **Shared Vocabularies**: Instead of returning JSON/XML with attributes tied to the names of the internal models, use standard vocabularies that are well thought out by standardization bodies (such as [schema.org](https://schema.org) or [IANA](https://www.iana.org/assignments/link-relations/link-relations.xhtml)). Even if you have to create your own type because a standard doesn't exist, define it explicitly to be decoupled from any changes that you can make to the internal model.

With Vulcan Architect, you can create APIs that follow all the REST principles and the Vulcan Guidelines without much effort.

## Why should I use it?

When creating a Hypermedia API, you must consider things like representation formats, relations between resources, vocabularies, and so on. Because of this, we built Vulcan Architect as a library that facilitates the development of Hypermedia APIs that follow all the principles, leaving you to worry only about your internal logic.

Therefore, you can focus on creating beautiful APIs that stand the test of time.

## How will it help me?

Vulcan Architect helps you by providing the following:

- JAX-RS writers for the most important Hypermedia formats, such as HAL or JSON-LD (with more coming).
- An easy way of representing your resources in a generic way so every representation can understand it, but following common Hypermedia patterns such as the Representor.
- A simple way of creating the different endpoints for your resources, which has many similarities with the JAX-RS approach.

Migrating your API from a REST JAX-RS implementation to Vulcan Architect is easy as pie!

## Can I try a sample Vulcan API online?

Absolutely! If you don't want to create your own API for now, you can use our test server.

Simply use your favorite REST-request client to make a GET request to:

    http://vulcan-vulcansample.wedeploy.io

To use Vulcan Architect APIs, you must specify an `accept` HTTP header. If you want to try a Hypermedia representation format, you can start with the following to order JSON-LD:

    accept: application/ld+json

You can alternatively start with the following to order HAL:

    accept: application/hal+json

## How do I start developing APIs with it?

Getting started with Vulcan Architect is simple. All you need is an OSGi container with JAX-RS.

To try all this quickly, you can use our [docker image](https://hub.docker.com/r/ahdezma/vulcan-whiteboard/). Simply run this on your terminal, specifying the folder in which module hot-deploy occurs:

    docker run -p 8080:8080 -v "/Users/YOUR_USER/deploy:/deploy" -d ahdezma/vulcan-whiteboard

As simple as that, you have a JAX-RS application with Vulcan Architect running in an OSGi container. You can access it by making a request to `http://localhost:8080`.

Now add these lines to your `pom.xml`:

```xml
<dependency>
  <groupId>com.liferay</groupId>
  <artifactId>com.liferay.vulcan.api</artifactId>
  <version>LATEST</version>
  <scope>provided</scope>
</dependency>
```

Or add these lines to your `build.gradle`:

```groovy
dependencies {
	provided 'com.liferay:com.liferay.vulcan.api:+'
}
```

Now you're ready to create your first Vulcan Architect resource!

Create a new Java class and annotate it with `@Component` to expose it as an OSGi component. Then implement the [`CollectionResource`](https://github.com/liferay/com-liferay-vulcan/blob/master/vulcan-api/src/main/java/com/liferay/vulcan/resource/CollectionResource.java) class of `vulcan-api`. You must provide two type arguments: the type of the model you want to expose, and the type of the identifier that uses that model. For example, if your model's type uses a `long` as an internal identifier, then use [`LongIdentifier`](https://github.com/liferay/com-liferay-vulcan/blob/master/vulcan-api/src/main/java/com/liferay/vulcan/resource/identifier/LongIdentifier.java).

Now you must implement three methods:

- `getName`: Provides the resource's name. This name is used internally in Vulcan Architect for different tasks, including URL creation.

- `buildRepresentor`: Builds the mapping between your internal model and the your chosen standard vocabulary (e.g. [schema.org](https://schema.org).

- `routes`: Builds the mapping between the operations supported for this resource and the methods that Vulcan Architect should call to complete them.

And that's it! Build your module's `jar` and deploy it in your OSGi container's hot deploy folder. When the module activates, you're good to go!

If you make a request to `http://localhost:8080` again, you should see a new endpoint that corresponds to the new resource you just created.

Now you're ready to start surfing the Hypermedia world!

## The Vulcan Project

The Vulcan project provides a set of guidelines and software to build evolvable APIs and consumers.

### [Vulcan Guidelines](https://vulcan.wedeploy.io/guidelines/)

An opinionated way to do RESTful APIs for *evolvability* and *discoverability*. Evolvability means that it's easy to add to and modify the API without breaking consumers. 

Discoverability is even more exciting--it means that the consumer can "learn" about new functionality added to the provider, from the provider itself. In Vulcan APIs, the provider controls navigation, forms, state changes, and more. The consumer can then leverage additions and modifications to the API. This simplifies consumers and lets them gain new functionality. Although this may seem like magic, it's standard in Vulcan APIs and consumers. 

### Vulcan Consumers

The Vulcan project also contains a client-side library to facilitate the development of consumers for Vulcan REST APIs (or any Hypermedia API). It also has "smart" functionality, like the the ability to automatically create a local graph to facilitate the construction of offline support. 

The consumer can control what the response includes (e.g., fields, embedded resources, etc.), and decide which hypermedia format best fits its needs (e.g., HAL, JSON-LD, etc.). 

- [Vulcan Consumer for Android](https://github.com/liferay-mobile/vulcan-consumer-android)
- Vulcan Consumer for iOS (coming soon)
- Vulcan Consumer for JS (coming soon)

## Contributing
Liferay welcomes any and all contributions! Please read the [CONTRIBUTING guide](https://github.com/liferay/liferay-portal/blob/master/CONTRIBUTING.markdown) for details on developing and submitting your contributions.

Pull requests with contributions should be sent to the GitHub user *liferay*. Those pull requests will be discussed and reviewed by the Engineering team before including them in the product.

## Bug Reporting and Feature Requests
Did you find a bug? Please file an issue for it at [https://issues.liferay.com](https://issues.liferay.com) following [Liferay's JIRA Guidelines](http://www.liferay.com/community/wiki/-/wiki/Main/JIRA), and select *Vulcan Architect* as the component.

If you'd like to suggest a new feature for Liferay, visit the [Ideas Dashboard](https://dev.liferay.com/participate/ideas) to submit and track the progress of your idea!

## License
This library is free software ("Licensed Software"); you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; including but not limited to, the implied warranty of MERCHANTABILITY, NONINFRINGEMENT, or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA