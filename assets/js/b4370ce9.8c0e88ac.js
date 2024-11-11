"use strict";(self.webpackChunk=self.webpackChunk||[]).push([[6074],{8468:(e,n,a)=>{a.r(n),a.d(n,{assets:()=>o,contentTitle:()=>l,default:()=>h,frontMatter:()=>r,metadata:()=>i,toc:()=>c});const i=JSON.parse('{"id":"rules/index-name","title":"index-name","description":"Why?","source":"@site/docs/rules/index-name.md","sourceDirName":"rules","slug":"/rules/index-name","permalink":"/liquibase-linter/docs/rules/index-name","draft":false,"unlisted":false,"tags":[],"version":"current","frontMatter":{"title":"index-name"},"sidebar":"docs","previous":{"title":"illegal-change-types","permalink":"/liquibase-linter/docs/rules/illegal-change-types"},"next":{"title":"index-tablespace","permalink":"/liquibase-linter/docs/rules/index-tablespace"}}');var t=a(4848),s=a(8453);const r={title:"index-name"},l=void 0,o={},c=[{value:"Why?",id:"why",level:2},{value:"Options",id:"options",level:2},{value:"Example Usage",id:"example-usage",level:2}];function d(e){const n={a:"a",code:"code",h2:"h2",li:"li",p:"p",pre:"pre",ul:"ul",...(0,s.R)(),...e.components};return(0,t.jsxs)(t.Fragment,{children:[(0,t.jsx)(n.h2,{id:"why",children:"Why?"}),"\n",(0,t.jsxs)(n.p,{children:["When ",(0,t.jsx)(n.a,{href:"http://www.liquibase.org/documentation/changes/create_index.html",children:"creating an index"}),", it's possible to omit a specific ",(0,t.jsx)(n.code,{children:"constraintName"})," for the index. This is hazardous, because the database vendor will automatically name it for you in an unpredictable way, which makes things difficult later if you want to reference or remove it."]}),"\n",(0,t.jsxs)(n.p,{children:["Also, you might already have a broad standard for object names - and be enforcing it with ",(0,t.jsx)(n.a,{href:"/liquibase-linter/docs/rules/object-name",children:"the object-name rule"})," - but you might also want a more specific rule concerning how indexes are named."]}),"\n",(0,t.jsxs)(n.p,{children:["This rule will fail if there is no ",(0,t.jsx)(n.code,{children:"constraintName"})," given when creating an index, or when configured with a pattern, will fail if the given name does not match the pattern."]}),"\n",(0,t.jsx)(n.h2,{id:"options",children:"Options"}),"\n",(0,t.jsxs)(n.ul,{children:["\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.code,{children:"pattern"})," - (regex, as string) optional regular expression that the name of any created index must adhere to"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.code,{children:"dynamicValue"})," - (string) Spring EL expression, with the ",(0,t.jsx)(n.a,{href:"https://github.com/liquibase/liquibase/blob/main/liquibase-core/src/main/java/liquibase/change/core/CreateIndexChange.java",children:(0,t.jsx)(n.code,{children:"CreateIndexChange"})})," instance as its expression scope, that should resolve to a string, and can then be interpolated in the pattern with ",(0,t.jsx)(n.code,{children:"{{value}}"})]}),"\n"]}),"\n",(0,t.jsx)(n.h2,{id:"example-usage",children:"Example Usage"}),"\n",(0,t.jsx)(n.p,{children:"To simply ensure that a name is always given:"}),"\n",(0,t.jsx)(n.pre,{children:(0,t.jsx)(n.code,{className:"language-json",children:'{\n    "rules": {\n        "index-name": true\n    }\n}\n'})}),"\n",(0,t.jsx)(n.p,{children:"To ensure that a pattern is matched, including the table name:"}),"\n",(0,t.jsx)(n.pre,{children:(0,t.jsx)(n.code,{className:"language-json",children:'{\n    "rules": {\n        "index-name": {\n            "pattern": "^{{value}}_I\\\\d$",\n            "dynamicValue": "tableName",\n            "errorMessage": "Index names must be the table name, suffixed with \'I\' and a number, e.g. FOO_I2"\n        }\n    }\n}\n'})})]})}function h(e={}){const{wrapper:n}={...(0,s.R)(),...e.components};return n?(0,t.jsx)(n,{...e,children:(0,t.jsx)(d,{...e})}):d(e)}},8453:(e,n,a)=>{a.d(n,{R:()=>r,x:()=>l});var i=a(6540);const t={},s=i.createContext(t);function r(e){const n=i.useContext(s);return i.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function l(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(t):e.components||t:r(e.components),i.createElement(s.Provider,{value:n},e.children)}}}]);