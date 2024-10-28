(window.webpackJsonp=window.webpackJsonp||[]).push([[49],{104:function(e,t,n){"use strict";n.r(t),n.d(t,"frontMatter",(function(){return l})),n.d(t,"metadata",(function(){return o})),n.d(t,"rightToc",(function(){return c})),n.d(t,"default",(function(){return u}));var a=n(2),i=n(6),r=(n(0),n(117)),l={title:"Using Rules",slug:"/rules/"},o={unversionedId:"rules/index",id:"rules/index",isDocsHomePage:!1,title:"Using Rules",description:"Liquibase Linter has several dozen rules that you can turn on and configure to suit your project.",source:"@site/docs/rules/index.md",slug:"/rules/",permalink:"/liquibase-linter/docs/rules/",version:"current",sidebar:"docs",previous:{title:"Retrofitting",permalink:"/liquibase-linter/docs/retrofitting"},next:{title:"Implementing a Custom Rule",permalink:"/liquibase-linter/docs/custom-rules"}},c=[{value:"Turning on a rule",id:"turning-on-a-rule",children:[]},{value:"Options",id:"options",children:[]},{value:"Multiple Configs",id:"multiple-configs",children:[]},{value:"Failure",id:"failure",children:[]}],s={rightToc:c};function u(e){var t=e.components,n=Object(i.a)(e,["components"]);return Object(r.b)("wrapper",Object(a.a)({},s,n,{components:t,mdxType:"MDXLayout"}),Object(r.b)("p",null,"Liquibase Linter has several dozen rules that you can turn on and configure to suit your project."),Object(r.b)("h2",{id:"turning-on-a-rule"},"Turning on a rule"),Object(r.b)("p",null,"No rules are turned on by default, but most can be turned on simply by adding a key with a ",Object(r.b)("inlineCode",{parentName:"p"},"true")," value to the ",Object(r.b)("inlineCode",{parentName:"p"},"rules")," object in the config file:"),Object(r.b)("pre",null,Object(r.b)("code",Object(a.a)({parentName:"pre"},{className:"language-json"}),'{\n    "rules": {\n        "no-duplicate-includes": true\n    }\n}\n')),Object(r.b)("p",null,"The value can also be an options object:"),Object(r.b)("pre",null,Object(r.b)("code",Object(a.a)({parentName:"pre"},{className:"language-json"}),'{\n    "rules": {\n        "no-duplicate-includes": {\n            "enabled": true\n        }\n    }\n}\n')),Object(r.b)("h2",{id:"options"},"Options"),Object(r.b)("p",null,"All rules also support these standard options (other than ",Object(r.b)("inlineCode",{parentName:"p"},"enabled"),"):"),Object(r.b)("ul",null,Object(r.b)("li",{parentName:"ul"},Object(r.b)("inlineCode",{parentName:"li"},"errorMessage")," - (string) override the default error message for this rule, which is output when the rule fails on a change. This can be useful if you are using a rule in a very targeted way and want to make it clear to the developer why it has failed. Most rules make the invalid value they found available to be interpolated with ",Object(r.b)("inlineCode",{parentName:"li"},"%s"),"."),Object(r.b)("li",{parentName:"ul"},Object(r.b)("inlineCode",{parentName:"li"},"condition")," - (string) - ",Object(r.b)("a",Object(a.a)({parentName:"li"},{href:"https://www.baeldung.com/spring-expression-language"}),"Spring EL expression")," that should resolve to a boolean, which if provided will decide whether the rule should be applied or not. The expression scope is as follows - ",Object(r.b)("ul",{parentName:"li"},Object(r.b)("li",{parentName:"ul"},Object(r.b)("a",Object(a.a)({parentName:"li"},{href:"https://github.com/liquibase/liquibase/blob/main/liquibase-core/src/main/java/liquibase/changelog/DatabaseChangeLog.java"}),Object(r.b)("inlineCode",{parentName:"a"},"DatabaseChangeLog"))," object available as ",Object(r.b)("inlineCode",{parentName:"li"},"changeLog")),Object(r.b)("li",{parentName:"ul"},Object(r.b)("a",Object(a.a)({parentName:"li"},{href:"https://github.com/liquibase/liquibase/blob/main/liquibase-core/src/main/java/liquibase/changelog/ChangeSet.java"}),Object(r.b)("inlineCode",{parentName:"a"},"ChangeSet"))," object available as ",Object(r.b)("inlineCode",{parentName:"li"},"changeSet")),Object(r.b)("li",{parentName:"ul"},Object(r.b)("a",Object(a.a)({parentName:"li"},{href:"https://github.com/liquibase/liquibase/blob/main/liquibase-core/src/main/java/liquibase/change/Change.java"}),Object(r.b)("inlineCode",{parentName:"a"},"Change"))," object available as ",Object(r.b)("inlineCode",{parentName:"li"},"change")),Object(r.b)("li",{parentName:"ul"},Object(r.b)("inlineCode",{parentName:"li"},"matchesContext")," helper function which can be used like ",Object(r.b)("inlineCode",{parentName:"li"},"matchesContext('foo', 'bar')"),". This function just delegates to the liquibase context matching method so the same logic applies."))),Object(r.b)("li",{parentName:"ul"},Object(r.b)("inlineCode",{parentName:"li"},"enableAfter")," - (string) allows you to specify a change log file name ",Object(r.b)("em",{parentName:"li"},"after")," which this rule should be enabled. See ",Object(r.b)("a",Object(a.a)({parentName:"li"},{href:"/liquibase-linter/docs/retrofitting"}),"Retrofitting")," for more detail.")),Object(r.b)("p",null,"Individual rules also support their own options; you can find these documented with those rules."),Object(r.b)("h2",{id:"multiple-configs"},"Multiple Configs"),Object(r.b)("p",null,"Though you might not need it often, you can specify multiple configs - with different options - for the same rule. You can do this by providing an array of rule config objects rather than just one, as in this example:"),Object(r.b)("pre",null,Object(r.b)("code",Object(a.a)({parentName:"pre"},{className:"language-json"}),'{\n    "rules": {\n        "object-name": [\n            {\n                "pattern": "^(?!_)[A-Z_0-9]+(?<!_)$",\n                "errorMessage": "Object name \'%s\' name must be uppercase and use \'_\' separation"\n            },\n            {\n                "pattern": "^POWER.*$",\n                "errorMessage": "Object name \'%s\' name must begin with \'POWER\'"\n            }\n        ]\n    }\n}\n')),Object(r.b)("p",null,'If you provide multiple configs, each applicable change/changeset/changelog will be checked with all of the configs in turn. A failure on any of the configs will be treated as a failure - in other words, your scripts have to pass against all the configs, so the logic is "AND" rather than "OR".'),Object(r.b)("h2",{id:"failure"},"Failure"),Object(r.b)("p",null,"Once a rule is switched on, it will be run against each of your scripts right after Liquibase parses them from their source format (e.g. XML). If a rule fails (that is, a script broke the rule) then Liquibase will exit with a ",Object(r.b)("inlineCode",{parentName:"p"},"ChangeLogParseException")," containing details of which change failed and why, and nothing will be run into the target database."))}u.isMDXComponent=!0},117:function(e,t,n){"use strict";n.d(t,"a",(function(){return b})),n.d(t,"b",(function(){return d}));var a=n(0),i=n.n(a);function r(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function l(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function o(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?l(Object(n),!0).forEach((function(t){r(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):l(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function c(e,t){if(null==e)return{};var n,a,i=function(e,t){if(null==e)return{};var n,a,i={},r=Object.keys(e);for(a=0;a<r.length;a++)n=r[a],t.indexOf(n)>=0||(i[n]=e[n]);return i}(e,t);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);for(a=0;a<r.length;a++)n=r[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(i[n]=e[n])}return i}var s=i.a.createContext({}),u=function(e){var t=i.a.useContext(s),n=t;return e&&(n="function"==typeof e?e(t):o(o({},t),e)),n},b=function(e){var t=u(e.components);return i.a.createElement(s.Provider,{value:t},e.children)},p={inlineCode:"code",wrapper:function(e){var t=e.children;return i.a.createElement(i.a.Fragment,{},t)}},h=i.a.forwardRef((function(e,t){var n=e.components,a=e.mdxType,r=e.originalType,l=e.parentName,s=c(e,["components","mdxType","originalType","parentName"]),b=u(n),h=a,d=b["".concat(l,".").concat(h)]||b[h]||p[h]||r;return n?i.a.createElement(d,o(o({ref:t},s),{},{components:n})):i.a.createElement(d,o({ref:t},s))}));function d(e,t){var n=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var r=n.length,l=new Array(r);l[0]=h;var o={};for(var c in t)hasOwnProperty.call(t,c)&&(o[c]=t[c]);o.originalType=e,o.mdxType="string"==typeof e?e:a,l[1]=o;for(var s=2;s<r;s++)l[s]=n[s];return i.a.createElement.apply(null,l)}return i.a.createElement.apply(null,n)}h.displayName="MDXCreateElement"}}]);