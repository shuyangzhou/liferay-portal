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

Vulcan Architect is part of the [Vulcan project](#the-vulcan-project), which aims to promote the creation of APIs designed to evolve over time. The project also provides a set of guidelines for API providers and consumers that can be implemented in any technology and Vulcan Consumer, a library to facilitate the creation of consumers of any hypermedia API.

Vulcan Architect is a server-side library to facilitate the creation of Vulcan REST APIs. Vulcan Architect is opinionated to reduce the amount of code API developers have to write. This is also achieved by implementing right out of the box well known patterns in REST APIs such as the Collection Pattern.

The two key techniques that make this possible are:

**Hypermedia**: yeah, the good old links and forms that we all use through a browser every day can also be applied to APIs to get the same great decoupling and flexibility.

**Shared Vocabularies**: instead of returning a JSON/XML with attributes tied to the names of the internal models, start with standard vocabularies that are well thought out by standardization bodies (such as [schema.org](https://schema.org) or [IANA](https://www.iana.org/assignments/link-relations/link-relations.xhtml)). Even if you have to create your own types (because an appropriate standard doesn’t exist), define it explicitly to be decoupled from any changes that you can make to the internal model.

With Vulcan Architect you can create APIs that follow all the REST principles (including HATEOAS) and the Vulcan Guidelines with significantly reduced effort.

## Why should I use it?

When creating a Hypermedia API there are many things you need to consider like representation formats, relations between resources, vocabularies, etc. Because of this, Architect was built as a library that facilitates developing a Hypermedia API that follows all the principles while having to worry only about your internal logic.

That way you can focus on creating beautiful APIs that will live longer and prosper.

## How will it help me?

Architect provides JAX-RS writers for the most important Hypermedia formats, such as HAL or JSON-LD (with more coming). It also provides an easy way of representing your resources, in a really generic way, so every representation can understand it, but following common Hypermedia patterns, such as the *Representor*.

It also provides a really simple way of creating the different endpoints for your resources, which has many similarities with the JAX-RS approach. So migrating your API from a REST JAX-RS implementation to Architect will be easy as pie.

## Can I try a sample Vulcan API online?

However, if you don't want to create your own API for now, but just want to try all this Hypermedia, Shared Vocabularies, Vulcan APIs, etc., you can use our test server for that.

As simply as use your favorite REST-request client to make a GET request to:

`http://vulcan-vulcansample.wedeploy.io`

To be able to use Vulcan Architect APIs you must specify an `accept` HTTP header. If you want to try a Hypermedia representation format, you can start with:

`accept: application/ld+json`

to request JSON-LD or:

`accept: application/hal+json`

to request HAL.

## How do I start developing APIs with it?

Creating your first API with Architect is very simple. All you need is an OSGi container with JAX-RS.

If you just want to try all this quickly, you can use our [docker image](https://hub.docker.com/r/ahdezma/vulcan-whiteboard/). Simply run this on your terminal (specifying the folder where to do module hot-deploying):

```
docker run -p 8080:8080 -v "/Users/YOUR_USER/deploy:/deploy" -d ahdezma/vulcan-whiteboard
```

As simple as that, you will have a JAX-RS application with Vulcan Architect running in an OSGi container, which you can consult by making a request to `http://localhost:8080`.

Now just add these lines to your `pom.xml`:

```xml
<dependency>
  <groupId>com.liferay</groupId>
  <artifactId>com.liferay.vulcan.api</artifactId>
  <version>LATEST</version>
  <scope>provided</scope>
</dependency>
```

or `build.gradle`:

```groovy
dependencies {
	provided 'com.liferay:com.liferay.vulcan.api:+'
}
```

And you're ready to create your first Vulcan Architect resource!

Create a new Java class and annotate it with `@Component` to expose it as an OSGi component. Then have it implement the [`CollectionResource`](https://github.com/liferay/com-liferay-vulcan/blob/master/vulcan-api/src/main/java/com/liferay/vulcan/resource/CollectionResource.java) class of `vulcan-api`. You will have to provide two type arguments: the type of the model you want to expose, and the type of identifier that uses that model, for example, [`LongIdentifier`](https://github.com/liferay/com-liferay-vulcan/blob/master/vulcan-api/src/main/java/com/liferay/vulcan/resource/identifier/LongIdentifier.java) (if your type uses a long number internally as an identifier).

Now you will simply have to implement three methods:

`getName`: to provide the name for this resource. This name is used internally in Architect for different tasks, including URL creation.

`buildRepresentor`: to build the mapping between your internal model and the standard vocabulary you have chosen (e.g. [schema.org](https://schema.org).

`routes`: to build the mapping between the operations supported for this resource and the methods that Architect should call to complete them.

And that's it! Build the `jar` of your module and deploy it in the folder you declared to make hot deployments, wait for it to activate... and that's it!

If you make a request to `http://localhost:8080` again, you should see a new declared endpoint corresponding to the new resource you have just created.

And start surfing the Hypermedia world!

## The Vulcan project

The Vulcan project provides a set of Guidelines and Software to build APIs and consumers designed to evolve.

### [Vulcan Guidelines](https://vulcan.wedeploy.io/guidelines/)

An opinionated way to do RESTful APIs for evolvability and discoverability.

Evolvability means that it’s easy to evolve the API without breaking consumers.

Discoverability is even more exciting. In Vulcan APIs, the provider controls the navigation, forms, state changes... which simplifies the consumers and allows them to "learn" certain new functionalities that didn’t exist when they were developed. Sounds like magic and it’s indeed pretty cool.

### Vulcan Consumers

A client-side library to facilitate developing consumers that consume Vulcan REST APIs (or any Hypermedia API, really). It also has some smart capabilities such as automatic creation of a local graph to facilitate building offline support.

The consumer can control what the response will include: which fields, embedded resources... and decide which hypermedia format fits its needs best (HAL, JSON-LD, etc.).

- [Vulcan Consumer for Android](https://github.com/liferay-mobile/vulcan-consumer-android)
- Vulcan Consumer for iOS (coming...)
- Vulcan Consumer for JS (coming...)

## Contributing
Liferay welcomes any and all contributions! Please read the [CONTRIBUTING guide](https://github.com/liferay/liferay-portal/blob/master/CONTRIBUTING.markdown) for details on developing and submitting your contributions.

Pull requests with contributions should be sent to the GitHub user *liferay*. Those pull requests will be discussed and reviewed by the Engineering team before including them in the product.

## Bug Reporting and Feature Requests
Did you find a bug? Please file an issue for it at [https://issues.liferay.com](https://issues.liferay.com) following [Liferay's JIRA Guidelines](http://www.liferay.com/community/wiki/-/wiki/Main/JIRA) and select *Vulcan Architect* as the component.

If you'd like to suggest a new feature for Liferay, visit the [Ideas Dashboard](https://dev.liferay.com/participate/ideas) to submit and track the progress of your idea!

## License
This library is free software ("Licensed Software"); you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; including but not limited to, the implied warranty of MERCHANTABILITY, NONINFRINGEMENT, or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA