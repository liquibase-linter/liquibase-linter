"use strict";(self.webpackChunk=self.webpackChunk||[]).push([[1432],{3586:(e,n,t)=>{t.r(n),t.d(n,{assets:()=>i,contentTitle:()=>r,default:()=>u,frontMatter:()=>o,metadata:()=>a,toc:()=>c});const a=JSON.parse('{"id":"rules/table-name-length","title":"table-name-length","description":"Why?","source":"@site/docs/rules/table-name-length.md","sourceDirName":"rules","slug":"/rules/table-name-length","permalink":"/liquibase-linter/docs/rules/table-name-length","draft":false,"unlisted":false,"tags":[],"version":"current","frontMatter":{"title":"table-name-length"},"sidebar":"docs","previous":{"title":"table-name","permalink":"/liquibase-linter/docs/rules/table-name"},"next":{"title":"unique-constraint-name","permalink":"/liquibase-linter/docs/rules/unique-constraint-name"}}');var s=t(4848),l=t(8453);const o={title:"table-name-length"},r=void 0,i={},c=[{value:"Why?",id:"why",level:2},{value:"Options",id:"options",level:2},{value:"Example Usage",id:"example-usage",level:2}];function h(e){const n={a:"a",code:"code",h2:"h2",li:"li",p:"p",pre:"pre",ul:"ul",...(0,l.R)(),...e.components};return(0,s.jsxs)(s.Fragment,{children:[(0,s.jsx)(n.h2,{id:"why",children:"Why?"}),"\n",(0,s.jsxs)(n.p,{children:["You might already be employing ",(0,s.jsx)(n.a,{href:"/liquibase-linter/docs/rules/object-name-length",children:"the object-name-length rule"})," to prevent names that are too long across all types of schema objects. If you also have conventions around constraints and indexes that involve using the table name as a prefix, you might want to override the maximum length for a table's name, to ensure you can always meet your other naming standards."]}),"\n",(0,s.jsxs)(n.p,{children:["For example, let's say you have your object names limited to 30 characters, and then create a table named ",(0,s.jsx)(n.code,{children:"APP_FAILED_PROPOSAL_VALIDATION"}),", which just fits. Now you want to add a primary key, and your convention is to suffix the table name with ",(0,s.jsx)(n.code,{children:"_PK"}),", meaning the correct constraint name would be ",(0,s.jsx)(n.code,{children:"APP_FAILED_PROPOSAL_VALIDATION_PK"})," - which is 33 characters long. So, you could limit your table names to 27 characters to guard against this without having to drop the naming standard."]}),"\n",(0,s.jsx)(n.p,{children:"This rule will fail if the given maximum length is exceeded by the name when creating or renaming a table."}),"\n",(0,s.jsx)(n.h2,{id:"options",children:"Options"}),"\n",(0,s.jsxs)(n.ul,{children:["\n",(0,s.jsxs)(n.li,{children:[(0,s.jsx)(n.code,{children:"maxLength"})," - (number) the maximum length of the name of any created or renamed table"]}),"\n"]}),"\n",(0,s.jsx)(n.h2,{id:"example-usage",children:"Example Usage"}),"\n",(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-json",children:'{\n    "rules": {\n        "table-name-length": {\n            "maxLength": 60\n        }\n    }\n}\n'})})]})}function u(e={}){const{wrapper:n}={...(0,l.R)(),...e.components};return n?(0,s.jsx)(n,{...e,children:(0,s.jsx)(h,{...e})}):h(e)}},8453:(e,n,t)=>{t.d(n,{R:()=>o,x:()=>r});var a=t(6540);const s={},l=a.createContext(s);function o(e){const n=a.useContext(l);return a.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function r(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(s):e.components||s:o(e.components),a.createElement(l.Provider,{value:n},e.children)}}}]);