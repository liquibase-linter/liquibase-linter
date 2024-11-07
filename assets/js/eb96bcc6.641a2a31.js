"use strict";(self.webpackChunk=self.webpackChunk||[]).push([[217],{1021:(e,n,i)=>{i.r(n),i.d(n,{assets:()=>o,contentTitle:()=>r,default:()=>u,frontMatter:()=>l,metadata:()=>s,toc:()=>c});const s=JSON.parse('{"id":"rules/index","title":"Using Rules","description":"Liquibase Linter has several dozen rules that you can turn on and configure to suit your project.","source":"@site/docs/rules/index.md","sourceDirName":"rules","slug":"/rules/","permalink":"/liquibase-linter/docs/rules/","draft":false,"unlisted":false,"tags":[],"version":"current","frontMatter":{"title":"Using Rules","slug":"/rules/"},"sidebar":"docs","previous":{"title":"Retrofitting","permalink":"/liquibase-linter/docs/retrofitting"},"next":{"title":"create-column-no-define-primary-key","permalink":"/liquibase-linter/docs/rules/create-column-no-define-primary-key"}}');var a=i(4848),t=i(8453);const l={title:"Using Rules",slug:"/rules/"},r=void 0,o={},c=[{value:"Turning on a rule",id:"turning-on-a-rule",level:2},{value:"Options",id:"options",level:2},{value:"Multiple Configs",id:"multiple-configs",level:2},{value:"Failure",id:"failure",level:2}];function h(e){const n={a:"a",code:"code",em:"em",h2:"h2",li:"li",p:"p",pre:"pre",ul:"ul",...(0,t.R)(),...e.components};return(0,a.jsxs)(a.Fragment,{children:[(0,a.jsx)(n.p,{children:"Liquibase Linter has several dozen rules that you can turn on and configure to suit your project."}),"\n",(0,a.jsx)(n.h2,{id:"turning-on-a-rule",children:"Turning on a rule"}),"\n",(0,a.jsxs)(n.p,{children:["No rules are turned on by default, but most can be turned on simply by adding a key with a ",(0,a.jsx)(n.code,{children:"true"})," value to the ",(0,a.jsx)(n.code,{children:"rules"})," object in the config file:"]}),"\n",(0,a.jsx)(n.pre,{children:(0,a.jsx)(n.code,{className:"language-json",children:'{\n    "rules": {\n        "isolate-ddl-changes": true\n    }\n}\n'})}),"\n",(0,a.jsx)(n.p,{children:"The value can also be an options object:"}),"\n",(0,a.jsx)(n.pre,{children:(0,a.jsx)(n.code,{className:"language-json",children:'{\n    "rules": {\n        "isolate-ddl-changes": {\n            "enabled": true\n        }\n    }\n}\n'})}),"\n",(0,a.jsx)(n.h2,{id:"options",children:"Options"}),"\n",(0,a.jsxs)(n.p,{children:["All rules also support these standard options (other than ",(0,a.jsx)(n.code,{children:"enabled"}),"):"]}),"\n",(0,a.jsxs)(n.ul,{children:["\n",(0,a.jsxs)(n.li,{children:[(0,a.jsx)(n.code,{children:"errorMessage"})," - (string) override the default error message for this rule, which is output when the rule fails on a change. This can be useful if you are using a rule in a very targeted way and want to make it clear to the developer why it has failed. Most rules make the invalid value they found available to be interpolated with ",(0,a.jsx)(n.code,{children:"%s"}),"."]}),"\n",(0,a.jsxs)(n.li,{children:[(0,a.jsx)(n.code,{children:"condition"})," - (string) - ",(0,a.jsx)(n.a,{href:"https://www.baeldung.com/spring-expression-language",children:"Spring EL expression"})," that should resolve to a boolean, which if provided will decide whether the rule should be applied or not. The expression scope is as follows -","\n",(0,a.jsxs)(n.ul,{children:["\n",(0,a.jsxs)(n.li,{children:[(0,a.jsx)(n.a,{href:"https://github.com/liquibase/liquibase/blob/main/liquibase-core/src/main/java/liquibase/changelog/DatabaseChangeLog.java",children:(0,a.jsx)(n.code,{children:"DatabaseChangeLog"})})," object available as ",(0,a.jsx)(n.code,{children:"changeLog"})]}),"\n",(0,a.jsxs)(n.li,{children:[(0,a.jsx)(n.a,{href:"https://github.com/liquibase/liquibase/blob/main/liquibase-core/src/main/java/liquibase/changelog/ChangeSet.java",children:(0,a.jsx)(n.code,{children:"ChangeSet"})})," object available as ",(0,a.jsx)(n.code,{children:"changeSet"})]}),"\n",(0,a.jsxs)(n.li,{children:[(0,a.jsx)(n.a,{href:"https://github.com/liquibase/liquibase/blob/main/liquibase-core/src/main/java/liquibase/change/Change.java",children:(0,a.jsx)(n.code,{children:"Change"})})," object available as ",(0,a.jsx)(n.code,{children:"change"})]}),"\n",(0,a.jsxs)(n.li,{children:[(0,a.jsx)(n.code,{children:"matchesContext"})," helper function which can be used like ",(0,a.jsx)(n.code,{children:"matchesContext('foo', 'bar')"}),". This function just delegates to the liquibase context matching method so the same logic applies."]}),"\n"]}),"\n"]}),"\n",(0,a.jsxs)(n.li,{children:[(0,a.jsx)(n.code,{children:"enableAfter"})," - (string) allows you to specify a change log file name ",(0,a.jsx)(n.em,{children:"after"})," which this rule should be enabled. See ",(0,a.jsx)(n.a,{href:"/liquibase-linter/docs/retrofitting",children:"Retrofitting"})," for more detail."]}),"\n"]}),"\n",(0,a.jsx)(n.p,{children:"Individual rules also support their own options; you can find these documented with those rules."}),"\n",(0,a.jsx)(n.h2,{id:"multiple-configs",children:"Multiple Configs"}),"\n",(0,a.jsx)(n.p,{children:"Though you might not need it often, you can specify multiple configs - with different options - for the same rule. You can do this by providing an array of rule config objects rather than just one, as in this example:"}),"\n",(0,a.jsx)(n.pre,{children:(0,a.jsx)(n.code,{className:"language-json",children:'{\n    "rules": {\n        "object-name": [\n            {\n                "pattern": "^(?!_)[A-Z_0-9]+(?<!_)$",\n                "errorMessage": "Object name \'%s\' name must be uppercase and use \'_\' separation"\n            },\n            {\n                "pattern": "^POWER.*$",\n                "errorMessage": "Object name \'%s\' name must begin with \'POWER\'"\n            }\n        ]\n    }\n}\n'})}),"\n",(0,a.jsx)(n.p,{children:'If you provide multiple configs, each applicable change/changeset/changelog will be checked with all of the configs in turn. A failure on any of the configs will be treated as a failure - in other words, your scripts have to pass against all the configs, so the logic is "AND" rather than "OR".'}),"\n",(0,a.jsx)(n.h2,{id:"failure",children:"Failure"}),"\n",(0,a.jsxs)(n.p,{children:["Once a rule is switched on, it will be run against each of your scripts right after Liquibase parses them from their source format (e.g. XML). If a rule fails (that is, a script broke the rule) then Liquibase will exit with a ",(0,a.jsx)(n.code,{children:"ChangeLogParseException"})," containing details of which change failed and why, and nothing will be run into the target database."]})]})}function u(e={}){const{wrapper:n}={...(0,t.R)(),...e.components};return n?(0,a.jsx)(n,{...e,children:(0,a.jsx)(h,{...e})}):h(e)}},8453:(e,n,i)=>{i.d(n,{R:()=>l,x:()=>r});var s=i(6540);const a={},t=s.createContext(a);function l(e){const n=s.useContext(t);return s.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function r(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(a):e.components||a:l(e.components),s.createElement(t.Provider,{value:n},e.children)}}}]);